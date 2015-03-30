import wx
import os
import sys
import string
import cv, cv2
import numpy


# class PiUIFrame(wx.Frame):
#     def __init__(self):
#         wx.Frame.__init__(self, None, -1, "PiUI", size=(250, 500))
#
#         self.display_panel = wx.Panel(self, -1)
#
#         # get first image
#         capture = cv2.VideoCapture(0)
#         self.capture = capture
#         ret, frame = self.capture.read()
#         height, width = frame.shape[:2]
#         frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
#         self.image = wx.ImageFromBuffer(width, height, frame)
#         self.buildBmp = wx.BitmapFromBuffer(width, height, frame.tostring())
#
#         self.Bind(wx.EVT_PAINT, self.on_paint)
#
#         self.timer = wx.Timer(self)
#         self.timer.Start(1000. / 20)
#
#         self.mm = wx.DisplaySize()
#
#         # wx.StaticBitmap(parent=self, bitmap=self.image.ConvertToBitmap())
#
#         self.Bind(wx.EVT_TIMER, self.next_frame)
#
#         self.ShowFullScreen(True, style=wx.FULLSCREEN_ALL)
#
#         self.timer = wx.Timer(self)
#         self.timer.Start(1000. / 15)
#
#     def on_paint(self, evt):
#         if self.image:
#             dc = wx.BufferedPaintDC(self.display_panel, self.image.ConvertToBitmap())
#         evt.Skip()
#
#     def next_frame(self, evt):
#         ret, frame = self.capture.read()
#         if ret:
#             frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
#             self.buildBmp.CopyFromBuffer(frame.tostring())
#             self.image = self.buildBmp. ConvertToImage()
#             self.image.Scale(self.mm[0], self.mm[1])
#             self.Refresh()
#         evt.Skip()
#
#
# class UIApp(wx.App):
#     def OnInit(self):
#         self.win = PiUIFrame()
#         self.win.Show(True)
#         self.SetTopWindow(self.win)
#         return True
#
#
# if __name__ == "__main__":
#     app = UIApp()
#     app.MainLoop()

class LiveFrame(wx.Frame):
    fps = 30


    def __init__(self, parent):
        wx.Frame.__init__(self, parent, -1, title="Live Camera Feed")

        self.SetDoubleBuffered(True)
        self.capture = None
        self.bmp = None
        self.mm = wx.DisplaySize()

        #set up camaera init
        self.capture = cv.CaptureFromCAM(0)
        frame = cv.QueryFrame(self.capture)
        if frame:
            cv.CvtColor(frame, frame, cv.CV_BGR2RGB)
            self.bmp = wx.BitmapFromBuffer(frame.width, frame.height, frame.tostring())
            # self.bmp = self.bmp.ConvertToImage().Resize((self.mm[0], self.mm[1]), (0, 0)).ConvertToBitmap()
        self.displayPanel = wx.Panel(self, -1)

        self.fpstimer = wx.Timer(self)
        self.fpstimer.Start(1000 / self.fps)
        self.Bind(wx.EVT_TIMER, self.onNextFrame, self.fpstimer)
        self.Bind(wx.EVT_PAINT, self.onPaint)

        self.ShowFullScreen(True, style=wx.FULLSCREEN_ALL)

    def updateVideo(self):
        frame = cv.QueryFrame(self.capture)
        if frame:
            cv.CvtColor(frame, frame, cv.CV_BGR2RGB)
            self.bmp.CopyFromBuffer(frame.tostring())
            # self.bmp = self.bmp.ConvertToImage().Resize((self.mm[0], self.mm[1]), (0, 0)).ConvertToBitmap()
            self.Refresh()

    def onNextFrame(self, evt):
        self.updateVideo()
        #self.Refresh()
        evt.Skip()

    def onPaint(self, evt):
        if self.bmp:
            wx.BufferedPaintDC(self.displayPanel, self.bmp)
        evt.Skip()


if __name__ == "__main__":
    app = wx.App()
    app.RestoreStdio()
    LiveFrame(None)
    app.MainLoop()