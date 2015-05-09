#!/bin/python
#coding=utf-8

from sendBaidu import sendBaidu

print "wav:",
test0 = sendBaidu(fileFormat = "wav", audioFile = "data/cmd.spx.wav")
test0.getResult()

print "pcm:",
test1 = sendBaidu(fileFormat = "pcm", audioFile = "data/cmd.pcm")
test1.getResult()

print "baidu:",
test3 = sendBaidu(fileFormat = "speex", audioFile = "data/baidu.spx")
test3.getResult()

print "opus:",
test2 = sendBaidu(fileFormat = "opus", audioFile = "data/cmd.ogg")
test2.getResult()

print "speex:",
test3 = sendBaidu(fileFormat = "speex", audioFile = "data/cmd.spx.ogg")
test3.getResult()