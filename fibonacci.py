# To find the n-th Fibonacci Number using formula
from math import sqrt
# import square-root method from math library
def nthFib(n):
	res = (((1+sqrt(5))**n)-((1-sqrt(5)))**n)/(2**n*sqrt(5))
	print(int(res),'is',str(n)+'th fibonacci number')

nthFib(13)
