/* **********************************************

  File Name: client.cpp

  Author: zhengdongjian@tju.edu.cn

  Created Time: Wed Mar 30 19:17:21 2016

*********************************************** */
#include <bits/stdc++.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>
using namespace std;

int main() {

	int sock = socket(AF_INET, SOCK_STREAM, 0);

	struct sockaddr_in ser_addr;
	memset(&ser_addr, 0, sizeof(ser_addr));
	ser_addr.sin_family = AF_INET; //ipv4
	ser_addr.sin_addr.s_addr = inet_addr("127.0.0.1");
	ser_addr.sin_port = htons(1234);
	connect(sock, (struct sockaddr*)&ser_addr, sizeof(ser_addr));

	char buffer[107];
	read(sock, buffer, sizeof(buffer) - 1);

	fprintf(stdout, "Message from server: %s\n", buffer);

	close(sock);

	return 0;
}
