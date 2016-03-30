/* **********************************************

  File Name: server.cpp

  Author: zhengdongjian@tju.edu.cn

  Created Time: Wed Mar 30 19:08:51 2016

*********************************************** */
#include <bits/stdc++.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>
using namespace std;

int main() {
	int ser_sock = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

	struct sockaddr_in ser_addr;
	memset(&ser_addr, 0, sizeof(ser_addr));
	ser_addr.sin_family = AF_INET; //IPv4
	ser_addr.sin_addr.s_addr = inet_addr("127.0.0.1");
	ser_addr.sin_port = htons(1234);
	bind(ser_sock, (struct sockaddr*)&ser_addr, sizeof(ser_addr));

	listen(ser_sock, 20);

	struct sockaddr_in clt_addr;
	socklen_t clt_addr_size = sizeof(clt_addr);

	while (getchar() != EOF) {
		int clt_sock = accept(ser_sock, (struct sockaddr*)&clt_addr, &clt_addr_size);

		char str[] = "Hello!";
		write(clt_sock, str, sizeof(str));

		close(clt_sock);
	}
	close(ser_sock);

	return 0;
}
