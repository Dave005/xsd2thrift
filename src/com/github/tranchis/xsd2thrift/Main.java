/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * XSD2Thrift
 * 
 * Copyright (C) 2009 Sergio Alvarez-Napagao http://www.sergio-alvarez.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 */
package com.github.tranchis.xsd2thrift;

import java.util.TreeMap;

import com.github.tranchis.xsd2thrift.marshal.IMarshaller;
import com.github.tranchis.xsd2thrift.marshal.ThriftMarshaller;

public class Main
{
	private static boolean	correct;
	private static String	usage = "xsd2thrift, version 0.1b\n" + 
			"\n" + 
			"usage: java xsd2thrift.jar [--thrift] filename\n" + 
			"\n" + 
			"  --thrift : convert to thrift\n" + 
			"";
	

	private static void usage(String error)
	{
		System.err.println(error);
		usage();
	}

	private static void usage()
	{
		System.err.println(usage);
		correct = false;
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		XSDParser				xp;
		TreeMap<String,String>	map;
		String					xsd;
		int						i;
		IMarshaller				im;
		
		correct = true;
		im = null;
		
		map = new TreeMap<String,String>();
		map.put("schema_._type", "BaseObject");
		map.put("EString", "string");
		map.put("EBoolean", "boolean");
		map.put("EInt", "integer");
		map.put("EDate", "long");
		map.put("EChar", "byte");
		map.put("EFloat", "decimal");
		
		if(args.length == 0 || args[args.length-1].startsWith("--"))
		{
			usage();
		}
		else
		{
			xsd = args[args.length - 1];
			xp = new XSDParser(xsd, map);
			
			i = 0;
			while(correct && i < args.length - 1)
			{
				if(args[i].equals("--thrift"))
				{
					if(im == null)
					{
						im = new ThriftMarshaller();
						xp.addMarshaller(im);
					}
					else
					{
						usage("Only one marshaller can be specified at a time.");
					}
				}
				else
				{
					usage();
				}
				
				i = i + 1;
			}
			
			if(im == null)
			{
				usage("A marshaller has to be specified.");
			}
			
			if(correct)
			{
				xp.parse();
			}
		}
	}
}
