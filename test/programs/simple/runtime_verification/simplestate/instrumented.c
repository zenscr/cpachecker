#line 1 "include.h"
void error_fn(void) 
{ 

  {
  ERROR: 
  goto ERROR;
}
}

#line 5 "include.h"
int __MONITOR_START_TRANSITION   = 0;

#line 6 "include.h"
int __MONITOR_END_TRANSITION   = 0;

#line 11 "include.h"
int k  ;

#line 13 "include.h"
int expensive(void) 
{ int result ;

  {
#line 14
  result = k;
#line 15
  result += 13;
#line 16
  result += 13;
#line 17
  result += 13;
#line 18
  result += 13;
#line 19
  result += 13;
#line 20
  result += 13;
#line 21
  result += 13;
#line 22
  result += 13;
#line 23
  return (result > 0);
}
}

#line 26 "include.h"
int checkProgramInvariant(void) 
{ int tmp ;
  int tmp___0 ;

  {
#line 27
  tmp = expensive();
#line 27
  if (tmp) {
#line 27
    if (k < 0) {
#line 27
      tmp___0 = 0;
    } else {
#line 27
      if (k > 100) {
#line 27
        tmp___0 = 0;
      } else {
#line 27
        tmp___0 = 1;
      }
    }
  } else {
#line 27
    tmp___0 = 1;
  }
#line 27
  return (tmp___0);
}
}

#line 2 "spec.work"
int __BLAST_error  ;

#line 3 "spec.work"
void __error__(void) 
{ 

  {
#line 5
  __BLAST_error = 0;
  ERROR: 
  goto ERROR;
}
}

#line 8 "spec.work"
void __BLAST___error__(void) 
{ 

  {
#line 10
  __BLAST_error = 0;
  BERROR: 
  goto BERROR;
}
}

void __initialize__(void) ;

#line 1 "nondetbranch.c"
extern int k ;

#line 8
extern int ( /* missing proto */  anti_op)() ;

#line 6
extern int ( /* missing proto */  nondet_int)() ;

#line 3 "nondetbranch.c"
int entry(void) 
{ int i ;
  int j ;
  int tmp ;

  {
  {
#line 4
  k = 0;
  {
#line 18 "spec.work"
  __MONITOR_START_TRANSITION = __MONITOR_START_TRANSITION;
#line 19
  tmp = checkProgramInvariant();
#line 19
  if (! tmp) {
#line 20
    error_fn();
  }
#line 22
  __MONITOR_END_TRANSITION = __MONITOR_END_TRANSITION;
  {

  }
  }
#line 6 "nondetbranch.c"
  tmp = nondet_int();
  }
#line 6
  if (tmp) {
    {
#line 7
    i = 0;
    {
#line 18 "spec.work"
    __MONITOR_START_TRANSITION = __MONITOR_START_TRANSITION;
#line 19
    tmp = checkProgramInvariant();
#line 19
    if (! tmp) {
#line 20
      error_fn();
    }
#line 22
    __MONITOR_END_TRANSITION = __MONITOR_END_TRANSITION;
    {

    }
    }

    }
#line 7 "nondetbranch.c"
    while (i < 1000000) {
      {
#line 8 "nondetbranch.c"
      anti_op();
#line 7
      i ++;
      {
#line 18 "spec.work"
      __MONITOR_START_TRANSITION = __MONITOR_START_TRANSITION;
#line 19
      tmp = checkProgramInvariant();
#line 19
      if (! tmp) {
#line 20
        error_fn();
      }
#line 22
      __MONITOR_END_TRANSITION = __MONITOR_END_TRANSITION;
      {

      }
      }

      }
    }
  } else {
    {
#line 11
    j = 0;
    {
#line 18 "spec.work"
    __MONITOR_START_TRANSITION = __MONITOR_START_TRANSITION;
#line 19
    tmp = checkProgramInvariant();
#line 19
    if (! tmp) {
#line 20
      error_fn();
    }
#line 22
    __MONITOR_END_TRANSITION = __MONITOR_END_TRANSITION;
    {

    }
    }

    }
#line 11 "nondetbranch.c"
    while (j < 100) {
      {
#line 12 "nondetbranch.c"
      k ++;
      {
#line 18 "spec.work"
      __MONITOR_START_TRANSITION = __MONITOR_START_TRANSITION;
#line 19
      tmp = checkProgramInvariant();
#line 19
      if (! tmp) {
#line 20
        error_fn();
      }
#line 22
      __MONITOR_END_TRANSITION = __MONITOR_END_TRANSITION;
      {

      }
      }
#line 11 "nondetbranch.c"
      j ++;
      {
#line 18 "spec.work"
      __MONITOR_START_TRANSITION = __MONITOR_START_TRANSITION;
#line 19
      tmp = checkProgramInvariant();
#line 19
      if (! tmp) {
#line 20
        error_fn();
      }
#line 22
      __MONITOR_END_TRANSITION = __MONITOR_END_TRANSITION;
      {

      }
      }

      }
    }
  }
#line 15
  return (1);
}
}

void __initialize__(void) 
{ 

  {

}
}

