#/bin/sh
JWSDP_DIR=/usr/local/jwsdp-1.4
javac -classpath .:$JWSDP_DIR/jaxrpc/lib/jaxrpc-impl.jar: SOAPTest.java
java -classpath .:$JWSDP_DIR/jaxrpc/lib/jaxrpc-api.jar:$JWSDP_DIR/jaxrpc/lib/jaxrpc-impl.jar:$JWSDP_DIR/jaxrpc/lib/jaxrpc-spi.jar:$JWSDP_DIR/jwsdp-shared/lib/activation.jar:$JWSDP_DIR/jwsdp-shared/lib/mail.jar:$JWSDP_DIR/saaj/lib/saaj-api.jar:$JWSDP_DIR/saaj/lib/saaj-impl.jar SOAPTest
