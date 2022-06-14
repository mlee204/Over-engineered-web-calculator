FROM php:7.4-apache

COPY src/ /var/www/html/
RUN a2enmod rewrite
RUN apt-get update && apt-get install zip unzip
# Install Composer
RUN curl --silent --show-error "https://getcomposer.org/installer" | php -- --install-dir=/usr/local/bin --filename=composer

# Install Composer dependencies
RUN composer install

EXPOSE 80