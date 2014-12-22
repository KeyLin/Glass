#!/usr/bin/python
import socket
import cv2
import numpy

TCP_IP = '192.168.1.117'
TCP_PORT = 50007 

sock = socket.socket()
sock.connect((TCP_IP, TCP_PORT))

print "Connect to " , TCP_IP
capture = cv2.VideoCapture(0)


while True:
	ret, frame = capture.read()

	encode_param=[int(cv2.IMWRITE_JPEG_QUALITY),90]
	result, imgencode = cv2.imencode('.jpg', frame, encode_param)
	data = numpy.array(imgencode)
	stringData = data.tostring()

	sock.send( str(len(stringData)).ljust(16));
	sock.send( stringData );
	decimg=cv2.imdecode(data,1)
	cv2.imshow('CLIENT',decimg)
	if cv2.waitKey(1) & 0xFF == ord('q'):
		break
sock.close()


cv2.destroyAllWindows() 
