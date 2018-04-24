#include <arpa/inet.h>
#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>

#define SRV_IP "127.0.0.1"
void diep(char *s)
{
	perror(s);
	exit(1);
}
#define BUFLEN 512
#define NPACK 10
#define PORT 8888
int main(void)
{
	struct sockaddr_in si_other;
	int sockfd, i, result;
	int slen=sizeof(si_other);
	char buf[BUFLEN];
	// Crear el socket (dominio, flujo/datagrama, protocoloUDP)
	sockfd=socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
	if (sockfd==-1)
		diep("socket");
	memset((char *) &si_other, 0, sizeof(si_other));

	// Nombrar el socket
	si_other.sin_family = AF_INET;
	si_other.sin_port = htons(PORT);
	result=inet_aton(SRV_IP, &si_other.sin_addr);
	if (result==0)
		diep("inet_aton() failed\n");
	for (i=0; i<NPACK; i++)
	{
		printf("Sending packet %d\n", i);
		//buffer = "This is packet i"
		sprintf(buf, "This is packet %d\n", i);

		// Enviar paquete
		result=sendto(sockfd, buf, BUFLEN, 0, (void *) &si_other, slen);
		if (result==-1)
		diep("sendto()");
	}
	close(sockfd);
	return 0;
}
