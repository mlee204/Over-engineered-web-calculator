FROM golang:1.16

WORKDIR /go/src/app
COPY ./src .

RUN go get -d -v ./...
RUN go install -v ./...

# This container exposes port 8080 to the outside world
EXPOSE 8080

ENTRYPOINT [ "go" ]

# Run the executable
CMD ["run", "./"]