import socket
import time
import os

# Currently set to oim-dev environment's ODE
UDP_IP = os.getenv('DOCKER_HOST_IP')
UDP_PORT = 46800
MESSAGE = "0022e12d18466c65c1493800000e00e4616183e85a8f0100c000038081bc001480b8494c4c950cd8cde6e9651116579f22a424dd78fffff00761e4fd7eb7d07f7fff80005f11d1020214c1c0ffc7c016aff4017a0ff65403b0fd204c20ffccc04f8fe40c420ffe6404cefe60e9a10133408fcfde1438103ab4138f00e1eec1048ec160103e237410445c171104e26bc103dc4154305c2c84103b1c1c8f0a82f42103f34262d1123198103dac25fb12034ce10381c259f12038ca103574251b10e3b2210324c23ad0f23d8efffe0000209340d10000004264bf00"

print("UDP target IP:", UDP_IP)
print("UDP target port:", UDP_PORT)

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM) # UDP

while True:
  time.sleep(5)
  print("sending BSM every 5 second")
  sock.sendto(bytes.fromhex(MESSAGE), (UDP_IP, UDP_PORT))
