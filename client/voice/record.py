# -*- coding: utf-8 -*-
#!/bin/python

import pyaudio
import wave

CHUNK = 1024
FORMAT = pyaudio.paInt16

CHANNELS = 1
RATE = 16000
RECORD_SECONDS = 5
WAVE_OUTPUT_FILENAME = "cmd.wav"

class Record(object):
	"""docstring for record"""
	def __init__(self, WAVE_OUTPUT_FILENAME,RECORD_SECONDS):
		super(record, self).__init__()
		self.WAVE_OUTPUT_FILENAME = WAVE_OUTPUT_FILENAME
		self.RECORD_SECONDS = RECORD_SECONDS

	def record_wav(self):
		pa = pyaudio.PyAudio()
		stream = pa.open(format=FORMAT,
				channels=CHANNELS,
				rate=RATE,
				input=True,
				frames_per_buffer=CHUNK)
		print "*recording*"
		frames = []

		for i in range(0, int(RATE/CHUNK*RECORD_SECONDS)):
			data = stream.read(CHUNK)
			frames.append(data)

		print "*done recording*"

		stream.stop_stream()
		stream.close()
		pa.terminate()

		wf = wave.open(WAVE_OUTPUT_FILENAME,"wb")
		wf.setnchannels(CHANNELS)
		wf.setsampwidth(p.get_sample_size(FORMAT))

		wf.setframerate(RATE)
		wf.writeframes(b''.join(frames))

		wf.close()

if __name__ == "__main__":
	test = Record(WAVE_OUTPUT_FILENAME="cmd.wav",RECORD_SECONDS=5)





