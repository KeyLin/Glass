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



#
# while True:
# 	ret, frame = capture.read()
#
# 	encode_param=[int(cv2.IMWRITE_JPEG_QUALITY),90]
# 	result, imgencode = cv2.imencode('.jpg', frame, encode_param)
# 	data = numpy.array(imgencode)
# 	stringData = data.tostring()
#
# 	sock.send( str(len(stringData)).ljust(16));
# 	sock.send( stringData );
# 	decimg=cv2.imdecode(data,1)
# 	cv2.imshow('CLIENT',decimg)
# 	if cv2.waitKey(1) & 0xFF == ord('q'):
# 		break
# sock.close()
#
#
# cv2.destroyAllWindows()


