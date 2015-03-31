import wx
import cv2
class LiveFrame(wx.Frame):
    fps = 30
    def __init__(self, parent):
        wx.Frame.__init__(self, parent, -1, title="Live Camera Feed")

        self.SetDoubleBuffered(True)
        self.capture = None
        self.bmp = None
        self.mm = wx.DisplaySize()

        #set up camaera init
        self.capture = cv2.CaptureFromCAM(0)
        frame = cv2.QueryFrame(self.capture)
        if frame:
            cv2.CvtColor(frame, frame, cv2.CV_BGR2RGB)
            self.bmp = wx.BitmapFromBuffer(frame.width, frame.height, frame.tostring())
            # self.bmp = self.bmp.ConvertToImage().Resize((self.mm[0], self.mm[1]), (0, 0)).ConvertToBitmap()
        self.displayPanel = wx.Panel(self, -1)

        self.fpstimer = wx.Timer(self)
        self.fpstimer.Start(1000 / self.fps)
        self.Bind(wx.EVT_TIMER, self.onNextFrame, self.fpstimer)
        self.Bind(wx.EVT_PAINT, self.onPaint)

        self.ShowFullScreen(True, style=wx.FULLSCREEN_ALL)

    def updateVideo(self):
        frame = cv2.QueryFrame(self.capture)
        if frame:
            cv2.CvtColor(frame, frame, cv2.CV_BGR2RGB)
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