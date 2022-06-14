import unittest
import multiply


class TestMultiply(unittest.TestCase):
    def test_multi(self):
        self.assertEqual(multiply.multiply_ints(5, 5), 25)
        self.assertEqual(multiply.multiply_ints(7, 2), 14)
        self.assertEqual(multiply.multiply_ints(-4, 2), -8)
        self.assertEqual(multiply.multiply_ints(-10, -3), 30)

    def test_invalid_inputs(self):
        self.assertRaises(TypeError, multiply.multiply_ints, '5', 5.2)
        self.assertRaises(TypeError, multiply.multiply_ints, '', '')
        self.assertRaises(TypeError, multiply.multiply_ints, 5.5, 4)
        self.assertRaises(TypeError, multiply.multiply_ints, "444", "hello")
        self.assertRaises(TypeError, multiply.multiply_ints, [2, 4], 5)
        self.assertRaises(TypeError, multiply.multiply_ints, {2: 4.5}, 5)

    def test_empty_parameters(self):
        self.assertRaises(TypeError, multiply.multiply_ints, None, None)
        self.assertRaises(TypeError, multiply.multiply_ints, 4, None)
        self.assertRaises(TypeError, multiply.multiply_ints, None, 13)


if __name__ == '__main__':
    unittest.main()
