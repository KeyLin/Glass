# -*- coding: utf-8 -*-
#!/bin/python

import pyaudio
import wave
import sys

from ctypes import *
from contextlib import contextmanager

ERROR_HANDLER_FUNC = CFUNCTYPE(None, c_char_p, c_int, c_char_p, c_int, c_char_p)
def py_error_handler(filename, line, function, err, fmt):
	pass
c_error_handler = ERROR_HANDLER_FUNC(py_error_handler)

asound = cdll.LoadLibrary('libasound.so')
# Set error handler
asound.snd_lib_error_set_handler(c_error_handler)

class Play(object):
	"""docstring for Play"""
	def __init__(self, CHUNK=1024):
		super(Play, self).__init__()
		self.CHUNK = CHUNK

	def play_wav(self,audio_file):
		wf = wave.open(audio_file, 'rb')

		pa = pyaudio.PyAudio()

		stream = pa.open(format=pa.get_format_from_width(wf.getsampwidth()),
		                channels=wf.getnchannels(),
		                rate=wf.getframerate(),
		                output=True)

		data = wf.readframes(self.CHUNK)

		while data != '':
		    stream.write(data)
		    data = wf.readframes(self.CHUNK)

		stream.stop_stream()
		stream.close()

		pa.terminate()

if __name__ == "__main__":
	# if len(sys.argv) < 2:
	# 	print("Plays a wave file.\n\nUsage: %s filename.wav" % sys.argv[0])
	# 	sys.exit(-1)
	test = Play()
	test.play_wav(audio_file="./data/output.wav")