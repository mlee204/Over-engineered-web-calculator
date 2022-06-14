# Dockerfile
FROM ruby:3.0

WORKDIR /code
COPY . /code

# install dependencies.
RUN gem install bundler

RUN bundle update

# Tell Docker to listen on port 8080.
EXPOSE 8080

CMD ["ruby", "webserver.rb"]