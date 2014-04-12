#!/usr/bin/perl
my $data ="";
my $req= "POST localhost:8800/ HTTP/1.0\r\n".
  "User-Agent: yep/1.1.1\r\n".
  "Host: example.com\r\n".
  "Content-Length: ".length ($data)."\r\n".
  "\r\n".
  $data."\r\n".
  "\r\n";
print length($req)."\n".$req;
