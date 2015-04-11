import bluetooth


def rfcomm_server_sdp():
    server_sock = bluetooth.BluetoothSocket(bluetooth.RFCOMM)

    port = 1
    # method : get_available_port can't use any more
    # #port = bluetooth.get_available_port(bluetooth.RFCOMM)

    server_sock.bind(("",port))
    server_sock.listen(1)

    uuid = "e8587008-297a-4676-9fc6-cc8ee6fa097c"
    bluetooth.advertise_service(server_sock,"FooBar Service",uuid)

    print "listening on port %d "% port
    client_sock,address = server_sock.accept()

    print "Accepted connection from " , address

    try:
        while True:
            data = client_sock.recv(1024)
            if len(data) == 0:
                break
            print "received [%s]" % data
    except IOError:
        pass


    print "disconnected"

    client_sock.close()
    server_sock.close()

    print "all done"


if __name__=="__main__":
	rfcomm_server_sdp()
