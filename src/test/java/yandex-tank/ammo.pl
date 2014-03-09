#!/usr/bin/perl
my $data ="login=max\npassword=12345";
my $req= "POST /authform HTTP/1.0\r\n".
  "User-Agent: yandex-tank/1.1.1\r\n".
  "Host: example.com\r\n".
  "Content-Length: ".length ($data)."\r\n".
  "User-Agent: xxx\r\n".
  "\r\n".
  $data."\r\n".
  "\r\n";
print length($req)."\n".$req;
