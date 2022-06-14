FROM python:3.9.2-slim

RUN apt-get update && apt-get install

# install postgress dependcies
RUN apt-get install -y \
  dos2unix \
  libpq-dev \
  libmariadb-dev-compat \
  libmariadb-dev \
  gcc \
  && apt-get clean

RUN pip install --upgrade pip
COPY requirements.txt /app/requirements.txt

WORKDIR /app


RUN pip install -r requirements.txt

COPY ./src /app

EXPOSE 5000

CMD ["python3", "app.py"]