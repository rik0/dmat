#! /usr/bin/python

import sys
from scipy import sparse,io

script_version = '1.0'

def main(argv=None):
	from optparse import OptionParser
	usgStr = 'Usage: %prog [options] rows cols filename.mtx'
	optParser = OptionParser(usage=usgStr, version='%prog '+script_version,
		description='Generates a random sparse Market-Matrix')
	
	optParser.add_option('-d', '--density', type='float', default=0.01,
		help='Set the sparse matrix density')
	optParser.add_option('-t', '--dtype', default='float',
		help='Set the sparse matrix element output type')
	optParser.add_option('-p', '--premultiplicator', type='float', default=1.0,
		help='Provide a multiplicator factor to apply before the element type conversion')
	
	(options,args) = optParser.parse_args()
	
	if len(args) != 3:
		optParser.error('Incorrect number of arguments ('+str(len(args))+' given, 3 required)')
	
	A = sparse.rand(int(args[0]), int(args[1]), density=options.density)
	B = (options.premultiplicator * A).astype(options.dtype)
	
	io.mmwrite(args[2],B,'Auto-generated sparse matrix by script '+sys.argv[0]+' v'+script_version)
	

if __name__ == '__main__':
	sys.exit(main())

