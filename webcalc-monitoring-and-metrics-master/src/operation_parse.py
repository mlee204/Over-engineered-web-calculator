import operator


def get_operator_calculate_value(function_operator, x, y):
    ops_dic = {"+": operator.add, "-": operator.sub, "*": operator.mul, "/": operator.truediv, "%": operator.mod}  # etc.
    return int(ops_dic[function_operator](x, y))
