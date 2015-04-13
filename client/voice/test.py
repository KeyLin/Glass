#!/bin/python
#coding=utf-8

from sendBaidu import sendBaidu

print "wav:",
test0 = sendBaidu(fileFormat = "wav", audioFile = "data/cmd.wav")
test0.getResult()

print "pcm:",
test1 = sendBaidu(fileFormat = "pcm", audioFile = "data/cmd.pcm")
test1.getResult()

print "opus:",
test2 = sendBaidu(fileFormat = "opus", audioFile = "data/cmd.opus")
test2.getResult()

print "speex:",
test3 = sendBaidu(fileFormat = "speex", audioFile = "data/cmd.spx")
test3.getResult()