# modulo_test.rb
require_relative 'modulo'
require 'test/unit'

class TestModulo < Test::Unit::TestCase
  def test_modulo_normal
    assert_equal(1, Modulo.modulo_int(5, 2))
    assert_equal(0, Modulo.modulo_int(10, 2))
    assert_equal(2, Modulo.modulo_int(17, 3))
    assert_equal(6, Modulo.modulo_int(44, 19))
    assert_equal(3, Modulo.modulo_int(-13, 4))

  end

  def test_modulo_divide_by_zero_error_check
    assert_raise(ZeroDivisionError) { Modulo.modulo_int(5, 0) }
    assert_raise(ZeroDivisionError) { Modulo.modulo_int(0, 0) }
    assert_raise(ZeroDivisionError) { Modulo.modulo_int(10, -0) }
  end

  def test_modulo_divide_invalid_parameters
    assert_raise(TypeError) { Modulo.modulo_int('4', '4') }
    assert_raise(TypeError) { Modulo.modulo_int('string', 'bad') }
    assert_raise(TypeError) { Modulo.modulo_int('', '') }
    assert_raise(TypeError) { Modulo.modulo_int(4, '4') }
    assert_raise(TypeError) { Modulo.modulo_int('55', 0) }
    assert_raise(TypeError) { Modulo.modulo_int([33, 22], [11]) }
    assert_raise(TypeError) { Modulo.modulo_int(nil, nil) }
  end
end