import cv2

class Camera:
    def __init__(self):
        self._cap = cv2.VideoCapture(0)
        if (self._cap.isOpened()):
            pass
        else:
            try:
                self._cap.open()
            except:
                raise "Open Camera Error"


    def openCamera(self):
        while(True):
            ret, frame = self._cap.read()

            gray = cv2.cvtColor(frame,cv2.COLOR_BGR2GRAY)

            cv2.imshow('frame',gray)

            if cv2.waitKey(1) & 0xFF == ord('q'):
                break

    def destory(self):
        self._cap.release()
        cv2.destoryAllWindows()


if __name__ == "__main__":
    camera = Camera()
    camera.openCamera()
    camera.destory()

