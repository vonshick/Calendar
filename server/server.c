#include <sys/types.h>
#include <sys/wait.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <netdb.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <pthread.h>

#define SERVER_PORT 1234
#define QUEUE_SIZE 5

struct thread_data_t {
    int socket;
    char sentence[300];
};

struct event {
    char name[100];
    char start_hour[3];
    char start_minutes[3];
    char end_hour[3];
    char end_minutes[3];
    char description[100];
    char day[3];
    char month[3];
    char year[5];
};

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

int server_socket_descriptor;
struct event *events[100];
int events_num = 0;
struct thread_data_t *client_data[50];
int clients_count = 0;

void createSample() {
    events[0] = malloc(sizeof(struct event));
    strcpy((*events[0]).name, "Hello from server");
    strcpy((*events[0]).start_hour, "1");
    strcpy((*events[0]).start_minutes, "2");
    strcpy((*events[0]).end_hour, "3");
    strcpy((*events[0]).end_minutes, "4");
    strcpy((*events[0]).description, "Have a nice day!");
    strcpy((*events[0]).day, "8");
    strcpy((*events[0]).month, "0"); //months are numbered from 0 to 11
    strcpy((*events[0]).year, "2019\n");

    events[1] = malloc(sizeof(struct event));
    strcpy((*events[1]).name, "Hi from server");
    strcpy((*events[1]).start_hour, "1");
    strcpy((*events[1]).start_minutes, "2");
    strcpy((*events[1]).end_hour, "3");
    strcpy((*events[1]).end_minutes, "4");
    strcpy((*events[1]).description, "Goodbye");
    strcpy((*events[1]).day, "8");
    strcpy((*events[1]).month, "0");
    strcpy((*events[1]).year, "2019\n");

    events_num = 2;
}

//save data read from socket as an element of the 'events' global array
void createEvent(char *data_chain) {
    printf("Tworzymy wydarzenie o następującej strukturze:\n");
    printf("%s\n", data_chain);
    events[events_num] = malloc(sizeof(struct event));
    //dividing string into substring with '~' as a delimeter
    char delim[] = "~";
    char *ptr = strtok(data_chain, delim);
    int i = 0;
    for (i = 0; i < 9; i++) {
        switch (i){
            case 0:
                strcpy((*events[events_num]).name, ptr);
                break;
            case 1:
                strcpy((*events[events_num]).start_hour, ptr);
                break;
            case 2:
                strcpy((*events[events_num]).start_minutes, ptr);
                break;
            case 3:
                strcpy((*events[events_num]).end_hour, ptr);
                break;
            case 4:
                strcpy((*events[events_num]).end_minutes, ptr);
                break;
            case 5:
                strcpy((*events[events_num]).description, ptr);
                break;
            case 6:
                strcpy((*events[events_num]).day, ptr);
                break;
            case 7:
                strcpy((*events[events_num]).month, ptr);
                break;
            case 8:
                strcpy((*events[events_num]).year, ptr);
                break;
        }
        ptr = strtok(NULL, delim);
    }
    printf("%s\n", (*events[events_num]).name);
    printf("%s\n", (*events[events_num]).start_hour);
    printf("%s\n", (*events[events_num]).start_minutes);
    printf("%s\n", (*events[events_num]).end_hour);
    printf("%s\n", (*events[events_num]).end_minutes);
    printf("%s\n", (*events[events_num]).description);
    printf("%s\n", (*events[events_num]).day);
    printf("%s\n", (*events[events_num]).month);
    printf("%s\n", (*events[events_num]).year);

    events_num += 1;
}

//create data chain which can be interpreted by client
void createDataChain(char *buf, int i) {
    strcpy(buf, (*events[i]).name);
    strcat(buf, "~");
    strcat(buf, (*events[i]).start_hour);
    strcat(buf, "~");
    strcat(buf, (*events[i]).start_minutes);
    strcat(buf, "~");
    strcat(buf, (*events[i]).end_hour);
    strcat(buf, "~");
    strcat(buf, (*events[i]).end_minutes);
    strcat(buf, "~");
    strcat(buf, (*events[i]).description);
    strcat(buf, "~");
    strcat(buf, (*events[i]).day);
    strcat(buf, "~");
    strcat(buf, (*events[i]).month);
    strcat(buf, "~");
    strcat(buf, (*events[i]).year);
}

void updateClients(int updating_client_socket) {
    printf("Aktualizujemy pozostałych klientów\n");
    int i = 0;
    for (i; i<clients_count; i++) {
        //client which made changes already have got them saved locally
        //sending them once again is unnecessary
        if(updating_client_socket == (*client_data[i]).socket){
            continue;
        }
        memset((*client_data[i]).sentence, 0x00, 300);
        strcpy((*client_data[i]).sentence, "update\n"); // 'update' is the message for client which tells about data updating start
        write((*client_data[i]).socket, (*client_data[i]).sentence, strlen((*client_data[i]).sentence) * sizeof(char));
        int j = 0;
        for(j; j<events_num; j++){
            memset((*client_data[i]).sentence, 0x00, 300);
            createDataChain((*client_data[i]).sentence, j);
            write((*client_data[i]).socket, (*client_data[i]).sentence, strlen((*client_data[i]).sentence) * sizeof(char)); // java client couldn't read string properly when size was written like 'sizeof((*th_data).sentence)'
        }
    }
}

void removeClient(int client_socket){
    int i = 0;
    for (i; i<clients_count; i++){
        if((*client_data[i]).socket == client_socket){
            printf("Closing connection to the client\n");
            int j = i;
            close((*client_data[i]).socket);
            for (j; j<clients_count-1; j++){
                client_data[j] = client_data[j+1];
            }
            free(client_data[clients_count]);
            clients_count-=1;
            printf("Connection to the client closed\n");
        }
    }
}

//remove event which has got all fields values equal to event sent by client
void removeEvent(struct thread_data_t *th_data){
    memset((*th_data).sentence, 0x00, 300);
    read((*th_data).socket, (*th_data).sentence, 300);
    printf("Usuwamy dane o następującej strukturze:\n");
    printf("%s\n", (*th_data).sentence);
    char delim[] = "~";
    char *ptr = strtok((*th_data).sentence, delim);
    int i = 0;
    for (i = 0; i < events_num; i++){
        if(strcmp((*events[i]).name, ptr) == 0 ){
            ptr = strtok(NULL, delim);
            if(strcmp((*events[i]).start_hour, ptr) == 0 ){
                ptr = strtok(NULL, delim);
                if(strcmp((*events[i]).start_minutes, ptr) == 0 ){
                    ptr = strtok(NULL, delim);
                    if(strcmp((*events[i]).end_hour, ptr) == 0 ){
                        ptr = strtok(NULL, delim);
                        if(strcmp((*events[i]).end_minutes, ptr) == 0 ){
                            ptr = strtok(NULL, delim);
                            if(strcmp((*events[i]).description, ptr) == 0 ){
                                ptr = strtok(NULL, delim);
                                if(strcmp((*events[i]).day, ptr) == 0 ){
                                    ptr = strtok(NULL, delim);
                                    if(strcmp((*events[i]).month, ptr) == 0 ){
                                        ptr = strtok(NULL, delim);
                                        if(strcmp((*events[i]).year, ptr) == 0 ){
                                            printf("Event removing...\n");
                                            int j = i;
                                            for (j; j<events_num-1; j++){
                                                events[j] = events[j+1];
                                            }
                                            free(events[events_num]);
                                            events_num-=1;
                                            printf("Event removed!\n");
                                            updateClients((*th_data).socket);
}}}}}}}}}}}


void *ThreadBehavior(void *t_data){
    pthread_detach(pthread_self());
    struct thread_data_t *th_data = (struct thread_data_t *)t_data;
    int fd = (*th_data).socket;
    int bytes_count;
    while (1) {
        bytes_count = read(fd, (*th_data).sentence, 300);
        if (bytes_count == 0) {
            printf("Connection to the client lost\n");
            removeClient((*th_data).socket);
            break;
        }
        //data chain created by user can not contain '~'. We use it to let server know about removing events
        if((*th_data).sentence[0] == '~') {
            removeEvent(th_data);
        } else {
            createEvent((*th_data).sentence);
            updateClients((*th_data).socket);
        }
        memset((*th_data).sentence, 0x00, 300);
    }
    pthread_exit(NULL);
}

void handleConnection(int connection_socket_descriptor) {
    int create_thread = 0;
//    struct thread_data_t *t_data = malloc(sizeof(struct thread_data_t));
    client_data[clients_count] = malloc(sizeof(struct thread_data_t));
    (*client_data[clients_count]).socket = connection_socket_descriptor;

    //send all existing events
    if (events_num > 0) {
        int i = 0;
        for (i = 0; i < events_num; i++) {
            memset((*client_data[clients_count]).sentence, 0x00, 300);
            createDataChain((*client_data[clients_count]).sentence,i);
            printf("To stworzyłem: %s\n", (*client_data[clients_count]).sentence);
            write((*client_data[clients_count]).socket, (*client_data[clients_count]).sentence, strlen((*client_data[clients_count]).sentence) * sizeof(char)); // java client couldn't read string properly when size was written like 'sizeof((*th_data).sentence)'
        }
    }
    // let client know that all events from server are sent
    memset((*client_data[clients_count]).sentence, 0x00, 300);
    strcpy((*client_data[clients_count]).sentence, "loading\n");
    write((*client_data[clients_count]).socket, (*client_data[clients_count]).sentence, strlen((*client_data[clients_count]).sentence) * sizeof(char)); // java client couldn't read string properly when size was written like 'sizeof((*th_data).sentence)'
    pthread_t thread;

    create_thread = pthread_create(&thread, NULL, ThreadBehavior, (void *)client_data[clients_count]);
    clients_count+=1;
    printf("Aktualnie podłączyło się %d klientów\n", clients_count);
    if (create_thread) {
        printf("Blad przy probie utworzenia watku do odczytu, kod bledu: %d\n", create_thread);
        exit(-1);
    }
}

void signal_callback_handler(int signum) {
    close(server_socket_descriptor);
    exit(signum);
}

int main(int argc, char *argv[]) {

   struct hostent* server_host_entity;

   if (argc != 3){
     fprintf(stderr, "To run server pass: %s <server_name> <port_number>\n", argv[0]);
     exit(1);
   }

   server_host_entity = gethostbyname(argv[1]);
   if (! server_host_entity) {
      fprintf(stderr, "%s: searching server IP address failed.\n", argv[0]);
      exit(1);
   }

    createSample();
    int connection_socket_descriptor;
    int bind_result;
    int listen_result;
    int reuse_addr_val = 1;
    struct sockaddr_in server_address;

    //inicjalizacja gniazda serwera
    memset(&server_address, 0, sizeof(struct sockaddr));
    server_address.sin_family = AF_INET;
    memcpy(&server_address.sin_addr.s_addr, server_host_entity->h_addr, server_host_entity->h_length);
    server_address.sin_port = htons(atoi(argv[2]));

    server_socket_descriptor = socket(AF_INET, SOCK_STREAM, 0);
    if (server_socket_descriptor < 0) {
        fprintf(stderr, "%s: Blad przy probie utworzenia gniazda..\n", argv[0]);
        exit(1);
    }

    signal(SIGINT, signal_callback_handler);

    setsockopt(server_socket_descriptor, SOL_SOCKET, SO_REUSEADDR, &reuse_addr_val, sizeof(int));

    bind_result = bind(server_socket_descriptor, (struct sockaddr *)&server_address, sizeof(struct sockaddr));
    if (bind_result < 0) {
        fprintf(stderr, "%s: Blad przy probie dowiazania adresu IP i numeru portu do gniazda.\n", argv[0]);
        exit(1);
    }

    listen_result = listen(server_socket_descriptor, QUEUE_SIZE);
    if (listen_result < 0) {
        fprintf(stderr, "%s: Blad przy probie ustawienia wielkosci kolejki.\n", argv[0]);
        exit(1);
    }

    while (1){
        connection_socket_descriptor = accept(server_socket_descriptor, NULL, NULL);
        if (connection_socket_descriptor < 0) {
            fprintf(stderr, "%s: Blad przy probie utworzenia gniazda dla polaczenia.\n", argv[0]);
            exit(1);
        }
        handleConnection(connection_socket_descriptor);
    }
    return (0);
}
