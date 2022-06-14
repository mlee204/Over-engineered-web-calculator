def multiply_ints(a, b):

    if isinstance(a, int) is False:
        raise TypeError(str(a) + " is not Type Integer")

    if isinstance(b, int) is False:
        raise TypeError(str(b) + " is not Type Integer")

    return a * b


