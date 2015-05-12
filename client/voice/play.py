# -*- coding: utf-8 -*-
#!/bin/python

import pyaudio
import wave
import sys

class Play(object):
	"""docstring for Play"""
	def __init__(self, audio_file, CHUNK=1024):
		super(Play, self).__init__()
		self.audio_file = audio_file

	def play_wav(self):
		wf = wave.open(audio_file, 'rb')

		pa = pyaudio.PyAudio()

		stream = pa.open(format=p.get_format_from_width(wf.getsampwidth()),
		                channels=wf.getnchannels(),
		                rate=wf.getframerate(),
		                output=True)

		data = wf.readframes(CHUNK)

		while data != '':
		    stream.write(data)
		    data = wf.readframes(CHUNK)

		stream.stop_stream()
		stream.close()

		pa.terminate()

if __name__ == "__main__":
	if len(sys.argv) < 2:
		print("Plays a wave file.\n\nUsage: %s filename.wav" % sys.argv[0])
		sys.exit(-1)
	test = Play(audio_file=sys.argv[1])