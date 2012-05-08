/* gcc -o test_epgm test_epgm.c -lzmq */

#include <stdio.h>
#include <zmq.h>

int main () {
  int mainrv = 0;

  void *context = zmq_init (1);
  if (context == NULL) {
    fprintf (stderr, "Error occurred during zmq_init(): %s\n", zmq_strerror (errno));
    mainrv = 1;
    goto end;
  }

  void *publisher = zmq_socket (context, ZMQ_PUB);
  if (publisher == 0) {
    fprintf (stderr, "Error occurred during zmq_socket(): %s\n", zmq_strerror (errno));
    mainrv = 1;
    goto exception0;
  }

  int rv = zmq_bind (publisher, "epgm://127.0.0.1;239.255.255.255:5561");
  if (rv != 0) {
    fprintf (stderr, "Error occurred during zmq_bind(): %s\n", zmq_strerror (errno));
    mainrv = 1;
    goto exception1;
  }

  fprintf(stderr, "\n\nSUCCESS! Ignore eventual warnings, it is normal as we used the loopback device to make it simple.\n\n");

  exception1:
  zmq_close (publisher);

  exception0:
  zmq_term (context);

  end:
  return mainrv;
}
