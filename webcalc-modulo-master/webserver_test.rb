require_relative 'webserver'
require_relative 'modulo'
require 'minitest/autorun'
require 'sinatra'
require 'sinatra/json'
require 'rack/test'

class WebserverTest < Minitest::Test
  include Rack::Test::Methods

  def app
    Webserver
  end

  def test_empty_parameters
    get '/'
    expected_response = { error: true, string: 'x parameter is required y parameter is required', answer: nil,
                          status: 400 }
    assert_path_exists '/'
    assert last_response.bad_request?
    assert_equal 'application/json', last_response.headers['Content-Type']
    assert_equal JSON.generate(expected_response), last_response.body
    assert_equal JSON.generate(expected_response).length.to_s, last_response.headers['Content-Length']
  end

  def test_mising_x_parameters
    x = 5
    get '/', x: x
    expected_response = { error: true, string: ' y parameter is required', answer: nil,
                          status: 400 }
    assert_path_exists '/', x: x
    assert last_response.bad_request?
    assert_equal 'application/json', last_response.headers['Content-Type']
    assert_equal JSON.generate(expected_response), last_response.body
    assert_equal JSON.generate(expected_response).length.to_s, last_response.headers['Content-Length']
  end

  def test_mising_y_parameters
    y = 5
    get '/', y: y
    expected_response = { error: true, string: 'x parameter is required', answer: nil,
                          status: 400 }
    assert_path_exists '/', y: y
    assert last_response.bad_request?
    assert_equal 'application/json', last_response.headers['Content-Type']
    assert_equal JSON.generate(expected_response), last_response.body
    assert_equal JSON.generate(expected_response).length.to_s, last_response.headers['Content-Length']
  end

  def test_with_valid_params
    x = 5
    y = 2
    get '/', x: x, y: y
    expected_response = { error: false, string: "#{x}%#{y}=1", answer: 1, status: 200 }
    assert_path_exists '/', x: x, y: y
    assert last_response.ok?
    assert_equal 'application/json', last_response.headers['Content-Type']
    assert_equal JSON.generate(expected_response), last_response.body
    assert_equal JSON.generate(expected_response).length.to_s, last_response.headers['Content-Length']
  end

  def test_with_valid_params_negative_number
    x = -5
    y = -2
    get '/', x: x, y: y
    expected_response = { error: false, string: '-5%-2=-1', answer: -1, status: 200 }
    assert_path_exists '/', x: x, y: y
    assert last_response.ok?
    assert_equal 'application/json', last_response.headers['Content-Type']
    assert_equal JSON.generate(expected_response), last_response.body
    assert_equal JSON.generate(expected_response).length.to_s, last_response.headers['Content-Length']
  end

  def test_with_invalid_y_param_string
    x = 5
    y = 'string'
    get '/', x: x, y: y
    expected_response = { error: true, string: "parameter y(#{y}) is not a valid Integer", answer: nil, status: 400 }
    assert_path_exists '/', x: x, y: y
    assert last_response.bad_request?
    assert_equal 'application/json', last_response.headers['Content-Type']
    assert_equal JSON.generate(expected_response), last_response.body
    assert_equal JSON.generate(expected_response).length.to_s, last_response.headers['Content-Length']
  end

  def test_with_invalid_param_float
    x = 4
    y = 33.3
    get '/', x: x, y: y
    expected_response = { error: true, string: "parameter y(#{y}) is not a valid Integer", answer: nil, status: 400 }
    assert_path_exists '/', x: x, y: y
    assert last_response.bad_request?
    assert_equal 'application/json', last_response.headers['Content-Type']
    assert_equal JSON.generate(expected_response), last_response.body
    assert_equal JSON.generate(expected_response).length.to_s, last_response.headers['Content-Length']
  end


  def test_with_param_x_nil
    x = nil
    y = 5

    get '/', x: x, y: y
    expected_response = { error: true, string: 'x parameter is required', answer: nil, status: 400 }
    assert_path_exists '/', x: x, y: y
    assert last_response.bad_request?
    assert_equal 'application/json', last_response.headers['Content-Type']
    assert_equal JSON.generate(expected_response), last_response.body
    assert_equal JSON.generate(expected_response).length.to_s, last_response.headers['Content-Length']
  end

  def test_divide_by_zero_error_response_to_user
    x = 5
    y = 0

    get '/', x: x, y: y
    expected_response = { error: true, string: "The divisor=#{y}, You can't divide by 0", answer: nil, status: 400 }
    assert_path_exists '/', x: x, y: y
    assert last_response.bad_request?
    assert_equal 'application/json', last_response.headers['Content-Type']
    assert_equal JSON.generate(expected_response), last_response.body
    assert_equal JSON.generate(expected_response).length.to_s, last_response.headers['Content-Length']
  end

  def test_page_not_found_response

    get '/testPageNotFound'
    assert last_response.not_found?
    expected_response = { error: true, string: 'Page not found: check url parameter syntax is correct', answer: nil, 
status: 404 }
    assert_equal 'application/json', last_response.headers['Content-Type']
    assert_equal JSON.generate(expected_response), last_response.body
    assert_equal JSON.generate(expected_response).length.to_s, last_response.headers['Content-Length']
  end

end
