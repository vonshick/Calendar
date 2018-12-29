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

//struktura zawierajÄca dane, ktĂłre zostanÄ przekazane do wÄtku

struct thread_data_t
{
    int socket;
    char sentence[300];
};

struct event
{
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
// struct thread_data_t client_data[50];
// int clients_count = 0;

void createSample(){
    events[0] = malloc(sizeof(struct event));
    strcpy((*events[0]).name, "siema_serwer");
    strcpy((*events[0]).start_hour, "1");
    strcpy((*events[0]).start_minutes, "2");
    strcpy((*events[0]).end_hour, "3");
    strcpy((*events[0]).end_minutes, "4");
    strcpy((*events[0]).description, "WITAM");
    strcpy((*events[0]).day, "12");
    strcpy((*events[0]).month, "11");
    strcpy((*events[0]).year, "2018");

    events[1] = malloc(sizeof(struct event));
    strcpy((*events[1]).name, "jol_serwer");
    strcpy((*events[1]).start_hour, "1");
    strcpy((*events[1]).start_minutes, "2");
    strcpy((*events[1]).end_hour, "3");
    strcpy((*events[1]).end_minutes, "4");
    strcpy((*events[1]).description, "WITAM");
    strcpy((*events[1]).day, "12");
    strcpy((*events[1]).month, "11");
    strcpy((*events[1]).year, "2018");
    events_num=2;
}
void createEvent(char *data_chain)
{
    events[events_num] = malloc(sizeof(struct event));
    char delim[] = "~";
    char *ptr = strtok(data_chain, delim);
    int i = 0;
    for (i = 0; i < 9; i++)
    {
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
        // //printf("'%s'\n", ptr);
        ptr = strtok(NULL, delim);
    }
    //printf("%s\n", (*events[events_num]).name);
    //printf("%s\n", (*events[events_num]).start_hour);
    //printf("%s\n", (*events[events_num]).start_minutes);
    //printf("%s\n", (*events[events_num]).end_hour);
    //printf("%s\n", (*events[events_num]).end_minutes);
    //printf("%s\n", (*events[events_num]).description);
    //printf("%s\n", (*events[events_num]).day);
    //printf("%s\n", (*events[events_num]).month);
    //printf("%s\n", (*events[events_num]).year);

    events_num += 1;
}

void createDataChain(char *buf, int i)
{
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
    strcat(buf, "\n");

    // return buf;
}

void updateClients(struct thread_data_t *th_data)
{
    //printf("No jestem!");
    write((*th_data).socket, (*th_data).sentence, strlen((*th_data).sentence) * sizeof(char)); // java client couldn't read string properly when size was written like 'sizeof((*th_data).sentence)'
}

void *ThreadBehavior(void *t_data)
{
    pthread_detach(pthread_self());
    struct thread_data_t *th_data = (struct thread_data_t *)t_data;
    int fd = (*th_data).socket;
    while (1)
    {
        read(fd, (*th_data).sentence, 300);
        //printf("Przeczytano: %s\n", (*th_data).sentence);
        createEvent((*th_data).sentence);
        // updateClients(th_data);
        memset((*th_data).sentence, 0x00, 300);
    }
    pthread_exit(NULL);
}

void handleConnection(int connection_socket_descriptor)
{
    int create_result = 0;
    struct thread_data_t *t_data = malloc(sizeof(struct thread_data_t));
    (*t_data).socket = connection_socket_descriptor;

    if (events_num > 0)
    {
        int i = 0;
        for (i = 0; i < events_num; i++)
        {
            printf("jezdym\n");
            memset((*t_data).sentence, 0x00, 300);
            createDataChain((*t_data).sentence,i);
            printf("To stworzyłem: %s\n", (*t_data).sentence);
            write((*t_data).socket, (*t_data).sentence, strlen((*t_data).sentence) * sizeof(char)); // java client couldn't read string properly when size was written like 'sizeof((*th_data).sentence)'
        }
    }
    printf("Nie wszedłym\n");
    // let client know that all events from server are sent
    memset((*t_data).sentence, 0x00, 300);
    strcpy((*t_data).sentence, "loaded");
    strcat((*t_data).sentence, "\n");
    write((*t_data).socket, (*t_data).sentence, strlen((*t_data).sentence) * sizeof(char)); // java client couldn't read string properly when size was written like 'sizeof((*th_data).sentence)'
    printf("Tu też żem jest\n");
    pthread_t thread1;
    create_result = pthread_create(&thread1, NULL, ThreadBehavior, (void *)t_data);

    if (create_result)
    {
        printf("Blad przy probie utworzenia watku nr 2, kod bledu: %d\n", create_result);
        exit(-1);
    }
}

void signal_callback_handler(int signum)
{
    close(server_socket_descriptor);
    exit(signum);
}

int main(int argc, char *argv[])
{

    createSample();
    //    int server_socket_descriptor;
    int connection_socket_descriptor;
    int bind_result;
    int listen_result;
    int reuse_addr_val = 1;
    struct sockaddr_in server_address;

    //inicjalizacja gniazda serwera

    memset(&server_address, 0, sizeof(struct sockaddr));
    server_address.sin_family = AF_INET;
    server_address.sin_addr.s_addr = htonl(INADDR_ANY);
    server_address.sin_port = htons(SERVER_PORT);

    server_socket_descriptor = socket(AF_INET, SOCK_STREAM, 0);
    if (server_socket_descriptor < 0)
    {
        fprintf(stderr, "%s: Blad przy probie utworzenia gniazda..\n", argv[0]);
        exit(1);
    }

    signal(SIGINT, signal_callback_handler);

    setsockopt(server_socket_descriptor, SOL_SOCKET, SO_REUSEADDR, &reuse_addr_val, sizeof(int));

    bind_result = bind(server_socket_descriptor, (struct sockaddr *)&server_address, sizeof(struct sockaddr));
    if (bind_result < 0)
    {
        fprintf(stderr, "%s: Blad przy probie dowiazania adresu IP i numeru portu do gniazda.\n", argv[0]);
        exit(1);
    }

    listen_result = listen(server_socket_descriptor, QUEUE_SIZE);
    if (listen_result < 0)
    {
        fprintf(stderr, "%s: Blad przy probie ustawienia wielkosci kolejki.\n", argv[0]);
        exit(1);
    }

    while (1)
    {
        connection_socket_descriptor = accept(server_socket_descriptor, NULL, NULL);
        if (connection_socket_descriptor < 0)
        {
            fprintf(stderr, "%s: Blad przy probie utworzenia gniazda dla polaczenia.\n", argv[0]);
            exit(1);
        }

        handleConnection(connection_socket_descriptor);
    }

    // close(server_socket_descriptor);
    // free(t_data);
    return (0);
}
