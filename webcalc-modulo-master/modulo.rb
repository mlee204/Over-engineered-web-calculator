# modulo.rb
module Modulo
  def self.modulo_int(x, y)
    if (x.is_a? Integer) && (y.is_a? Integer)
      x % y
    else
      raise TypeError, "x(#{x}) and y(#{y}) must be Integers"
    end
  end
end
