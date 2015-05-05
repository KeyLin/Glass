#coding=utf-8
import urllib,urllib2
import requests
import json
from requests import Request,Session
import ConfigParser
import base64
import os

class sendBaidu(object):
	"""docstring for sendBaidu"""
	def __init__(self, fileFormat, audioFile):
		super(sendBaidu, self).__init__()
		config = ConfigParser.ConfigParser()
		config.read('config.ini')

		self.cuid = config.get('baidu','cuid')
		self.apiKey = config.get('baidu','apiKey')
		self.secretKey = config.get('baidu','secretKey')
		self.tokenUrl = config.get('baidu','tokenUrl')
		self.serverUrl = config.get('baidu','serverUrl')
		self.fileFormat = fileFormat
		self.audioFile = audioFile 
		

	def getToken(self):
		getTokenURL = self.tokenUrl + "&client_id=" + self.apiKey + "&client_secret=" + self.secretKey
		#print getTokenURL
		f = urllib.urlopen(getTokenURL)
		try:
			access_token =  eval(f.read())['access_token']
		except:
			print " Try to refresh your auth code"
			exit(0)
		return access_token	


	def decodeFile(self):
		with open(self.audioFile,"r") as f:
			data = f.read()
			data_base64 = base64.b64encode(data) 
		if data_base64:
			return data_base64
		else:
			print "Failed encode the file to base64"
			return None 

    
	def sendAudio(self):
		content_length = 0
		file_len = os.path.getsize(self.audioFile)
		body = self.decodeFile()
		access_token = self.getToken()
		data_json = {
			"format" : self.fileFormat,
			"rate"   : 16000,
			"channel": 1,
			"cuid"   : self.cuid,
			"token"  : access_token,
			"len"    : file_len,
			"speech" : body,
		}

		headers = {
			"content-type":"application/json",
			"charset" : "utf-8",
		}

		r = requests.post(self.serverUrl, headers = headers, data = json.dumps(data_json))

		return r


	def getResult(self):
		result = self.sendAudio()
		#print type(result)
		result = result.json()
		#print type(result)
		if result.get('err_no') == 0:
			text = "".join(result.get('result')).encode('utf-8')
			print text
		else:
			err_no = result['err_no']
			err_msg = "".join(result.get('err_msg')).encode('utf-8')
			sn = "".join(result.get('sn')).encode('utf-8')
			print "err_no:"+str(err_no)
			print "err_msg:"+err_msg
			print "sn:"+sn
			#exit(0)
		#print str(result)

if __name__ == "__main__":
	test = sendBaidu(fileFormat = "pcm", audioFile = "data/cmd.pcm")
	test.getResult()
