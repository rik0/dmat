#! /usr/bin/python

import sys
from scipy import sparse,io

script_version = '1.0'

def main(argv=None):
	from optparse import OptionParser
	usgStr = 'Usage: %prog [options] mat1.mtx mat2.mtx matout.mtx'
	optParser = OptionParser(usage=usgStr, version='%prog '+script_version,
		description='Multiplies two Market-Matrix matrices and stores the result')
	
	(options,args) = optParser.parse_args()
	
	if len(args) != 3:
		optParser.error('Incorrect number of arguments ('+str(len(args))+' given, 3 required)')
	
	A = io.mmread(args[0])
	B = io.mmread(args[1])
	
	io.mmwrite(args[2],A*B,'Auto-generated sparse matrix by script '+sys.argv[0]+' v'+script_version)
	

if __name__ == '__main__':
	sys.exit(main())

