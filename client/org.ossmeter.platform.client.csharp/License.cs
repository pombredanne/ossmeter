using System;
using System.Net;
using System.IO;
using Newtonsoft.Json;
using System.Collections.Generic;
using Newtonsoft.Json.Linq;

namespace ossmeterclient
{
	public class License : NamedElement 
	{

		protected string url { get; set; }
	}
}
