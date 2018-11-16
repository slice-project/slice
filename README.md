### SLICE Github Repository ###
> This is full source code repository of SLICE project

> SLICE is acronym of "Self-Learnable IoT Common Software Engine".

### Structure of SLICE github Repository ###
> We devide our source code based on the category like following.

> SLICE main repository (this repository) [slice](https://github.com/slice-project/slice)

> SLICE source-code without distribution [slice-source](https://github.com/slice-project/slice-source)

> SLICE Framework distribution [slice-win32-dist](https://github.com/slice-project/slice-win32-dist)/[slice-macosx-dist](https://github.com/slice-project/slice-macosx-dist) 

> SLICE Engine distribution [slice-engine-dist](https://github.com/slice-project/slice-macosx-dist)

> SLICE example implementations [slice-examples](https://github.com/slice-project/slice-examples)

### Paper Publication ###
- YoungHo Suh, SungPil Woo, DongHwan Park. 

"SLICE : Self-Learnable IoT Common Software Engine" IoT2018, The 8th International Conference on Internet of Things (IoT 2018), Santa Barbara, USA, Oct. 2018. 


[URL](https://dl.acm.org/citation.cfm?doid=3277593.3277603).

### Specification ###
> Java 1.8

> Maven

### Contact ###

> Project Leader:
>> Donghwan Park,  Electronics and Telecommunication Research Institute (ETRI),  

>> Principal Member of Engineering Staff

>> dhpark@etri.re.kr

> Maintainer:
>> Youngho Suh, Electronics and Telecommunication Research Institute (ETRI)

>> Principal Member of Engineering Staff 

>> yhsuh@etri.re.kr

>> Sungpil Woo, Electronics and Telecommunication Research Institute (ETRI), 

>> Researcher

>> woosungpil@etri.re.kr

<h2><a id="user-content-get-started" class="anchor" href="https://github.com/IoTKETI/oneM2MTester/wiki#get-started" aria-hidden="true" data-mce-href="https://github.com/IoTKETI/oneM2MTester/wiki#get-started"></a>Get Started</h2><ul><li>clone SLICE Framework from <code>https://github.com/slice-project/slice</code><br></li><li>go to <code>org.etri.slice.distribution</code> folder<ul><li>go to <code>macosx</code> folder (mac)&nbsp;</li><li>go to <code>win32</code> folder (windows)<ul><li>go to <code>eclipse</code> folder<br><ul><li>launch <code>slice-tools.exe</code><br data-mce-bogus="1"></li></ul></li></ul></li></ul></li><li>import <code>org.etri.slice.apps</code> as general project<br></li><li>write agent description in <code>*.adl</code> file<br data-mce-bogus="1"><ul><li>'target source code generated'</li></ul></li><li>launch eclipse</li><li>Build generated source code using 'Maven'</li><li>Run SLICE engine on the edge. (run_slice.bat, run_slice.sh)</li>
<li>*** we will release final version soon.</li>
</ul>
