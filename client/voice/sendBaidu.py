import urllib,urllib2
import requests 
from file_to_base64 import file_to_base64
client_id = "OhqSXEmuAopiyr7LMWXcDs73"
response_type = "code"
redirect_uri = "oob"

getUrl = "http://openapi.baidu.com/oauth/2.0/authorize"

params = urllib.urlencode( {'client_id' : client_id, \
							'response_type':response_type,\
							'redirect_uri' : redirect_uri})

code = "883a21b796aa6c86d6c533547fb80741"

params = urllib.urlencode( 
		{
			'grant_type': 'authorization_code',
			'code' 		: code,
			'client_id' : 'OhqSXEmuAopiyr7LMWXcDs73',
			'client_secret' : 'IMfvWF4ygtxIzmt5lT5qp0EimSIkqbb2',
			'redirect_uri' : 'oob'
		}
)

testUrl = "https://openapi.baidu.com/oauth/2.0/token"
f = urllib.urlopen("https://openapi.baidu.com/oauth/2.0/token?%s"%params)

try:
	access_token =  eval(f.read())['access_token']
	print "Successfully get " + access_token

except:
	print " Try to refresh your auth code"




voiceParams =  { 
	'cuid' : 'Voice',
	'token' : access_token}


serverUrl = "http://vop.baidu.com/server_api" 
cuid = "voiceTest"

httpUrl = serverUrl + "?cuid="+cuid+"&token="+access_token+"&lan=zh" 

headers = { "content-type": "audio/wav;rate=8000",'content-length':'40000' }

body = ""
with open("cmd","rb") as file:
	body = file_to_base64("cmd")

r = requests.post(httpUrl,headers=headers,body=body)

print r.read()

