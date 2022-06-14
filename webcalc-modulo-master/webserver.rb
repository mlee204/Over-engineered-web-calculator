require 'sinatra'
require 'sinatra/json'
require 'sinatra/cors'
require 'sinatra/param'
require_relative 'modulo'

class Webserver < Sinatra::Base
  helpers Sinatra::Param
  set :bind, '0.0.0.0'
  set :allow_origin, '*'
  set :port, '8080'

  output = {
    error: false,
    string: '',
    answer: nil,
    status: 200

  }

  get '/' do
    response['Access-Control-Allow-Origin'] = '*'
    response['Content-Type'] = 'application/json'
    x = params['x']
    y = params['y']

    output_string = ''
    status_code = 200
    error_flag = false

    begin
      x = Integer(x)
    rescue ArgumentError => e
      error_flag = true
      status_code = 400
      output_string << "parameter x(#{x}) is not a valid Integer"
    rescue TypeError => e
      error_flag = true
      status_code = 400
      output_string << 'x parameter is required'
    end

    begin
      y = Integer(y)
    rescue ArgumentError => e
      error_flag = true
      status_code = 400
      output_string << "parameter y(#{y}) is not a valid Integer"
    rescue TypeError => e
      error_flag = true
      status_code = 400
      output_string << ' y parameter is required'
    end

    if x.is_a?(Integer) && y.is_a?(Integer)
      begin
        ans = Modulo.modulo_int(x, y)
      rescue ZeroDivisionError => e
        error_flag = true
        status_code = 400
        output_string << "The divisor=#{y}, You can't divide by 0"
      rescue TypeError => e
        error_flag = true
      else
        output_string << "#{x}%#{y}=#{ans}"
      end
    end

    output[:string] = output_string
    output[:answer] = ans
    output[:error] = error_flag
    output[:status] = status_code

    # set http status code
    status status_code
    return json(output)
  end

  not_found do
    # catch page not found error
    output = {
      error: true,
      string: 'Page not found: check url parameter syntax is correct',
      answer: nil,
      status: 404

    }
    status 404
    return json(output)
  end

  get '/service-discovery' do
    response['Access-Control-Allow-Origin'] = '*'
    response['Content-Type'] = 'application/json'

    discovery_output = {
      "service": 'modulo',
      "url parameter 1": 'x',
      "url parameter 2": 'y',
      "operator": '%',
      "parameter 1 data type": 'Integer',
      "parameter 2 data type": 'Integer',
      "returns": 'x%y',
      "return type": 'Integer'

    }

    status 200
    return json(discovery_output)

  end

  # start the server if ruby file executed directly
  run! if app_file == $0
end
