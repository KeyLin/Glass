# -*- coding: utf-8 -*-
#!/bin/python

import pyaudio
import wave

from ctypes import *
from contextlib import contextmanager

ERROR_HANDLER_FUNC = CFUNCTYPE(None, c_char_p, c_int, c_char_p, c_int, c_char_p)
def py_error_handler(filename, line, function, err, fmt):
	pass
c_error_handler = ERROR_HANDLER_FUNC(py_error_handler)

asound = cdll.LoadLibrary('libasound.so')
# Set error handler
asound.snd_lib_error_set_handler(c_error_handler)

class Record(object):
	"""docstring for record"""
	def __init__(self, WAVE_OUTPUT_FILENAME, RECORD_SECONDS=3, CHANNELS=1, RATE=16000, CHUNK=1024, FORMAT=pyaudio.paInt16):
		super(Record, self).__init__()
		self.WAVE_OUTPUT_FILENAME = WAVE_OUTPUT_FILENAME
		self.RECORD_SECONDS = RECORD_SECONDS
		self.CHANNELS = CHANNELS
		self.RATE = RATE
		self.CHUNK = 1024
		self.FORMAT = pyaudio.paInt16

	def record_wav(self):
		pa = pyaudio.PyAudio()
		stream = pa.open(format=self.FORMAT,
				channels=self.CHANNELS,
				rate=self.RATE,
				input=True,
				frames_per_buffer=self.CHUNK)
		print "*recording*"
		frames = []

		for i in range(0, int(self.RATE/self.CHUNK*self.RECORD_SECONDS)):
			data = stream.read(self.CHUNK)
			frames.append(data)

		print "*done recording*"

		stream.stop_stream()
		stream.close()
		pa.terminate()

		wf = wave.open(self.WAVE_OUTPUT_FILENAME,"wb")
		wf.setnchannels(self.CHANNELS)
		wf.setsampwidth(pa.get_sample_size(self.FORMAT))

		wf.setframerate(self.RATE)
		wf.writeframes(b''.join(frames))

		wf.close()

if __name__ == "__main__":
	test = Record(WAVE_OUTPUT_FILENAME="./data/output.wav",RECORD_SECONDS=5)
	test.record_wav()





