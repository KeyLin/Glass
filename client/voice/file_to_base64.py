#!/bin/python


import base64

def file_to_base64(FILE_NAME):
	with open(FILE_NAME,"rb") as f:
		print "Open file succeed"
		data = f.read()
		
		data_base64 = data.encode("base64")
	
	if data_base64:
		print "Succeed"
		return data_base64
	else:
		print "Failed"
		return None 

if __name__ == "__main__":
	file_to_base64("test.wmv")
