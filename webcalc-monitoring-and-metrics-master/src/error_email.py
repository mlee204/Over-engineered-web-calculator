import smtplib
import ssl
import yaml
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart


def send_error_email(service, expected, actual, response_time, error_type):
    # load config info
    config = yaml.safe_load(open('config.yaml'))
    sender_email = config['sender_email']
    receiver_email = config['receiver_email']
    password = config['password']
    port = 465  # For SSL
    smtp_server = "smtp.gmail.com"

    message = MIMEMultipart("alternative")
    message["Subject"] = "Alert:" + error_type + " for: " + service + " function"
    message["From"] = sender_email
    message["To"] = receiver_email

    text = """\
     Service: """ + service + """
     Error: """ + error_type + """
     Expected: """ + str(expected) + """
     Actual: """ + str(actual) + """
     Response Time: """ + str(response_time)

    part1 = MIMEText(text, "plain")
    message.attach(part1)

    context = ssl.create_default_context()
    with smtplib.SMTP_SSL(smtp_server, port, context=context) as server:
        server.login(sender_email, password)
        server.sendmail(sender_email, receiver_email, message.as_string())
