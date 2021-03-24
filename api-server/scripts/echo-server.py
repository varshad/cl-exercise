# Echo server program
import socket, time

HOST = ''                 # Symbolic name meaning all available interfaces
PORT = 50000              # Arbitrary non-privileged port
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((HOST, PORT))
s.listen(1)
starttime = time.time()

conn, addr = s.accept()
print 'Connected to', addr
while 1:
  data = conn.recv(1024)
  if not data: break
  if data == 'START':
    while 1:
      conn.sendall(data)
      time.sleep(60.0 - ((time.time() - starttime) % 60.0))
conn.close()
