/* Generated by CIL v. 1.3.7 */
/* print_CIL_Input is true */

#line 14 "../versisec/bind/progs1/../lib/stubs.h"
typedef int size_t;
#line 7 "../versisec/bind/progs1/../bind.h"
typedef char u_char;
#line 8 "../versisec/bind/progs1/../bind.h"
typedef int u_int;
#line 27 "../versisec/bind/progs1/../lib/stubs.h"
extern unsigned int strlen(char const   *s ) ;
#line 53
extern void *r_memcpy(void *dest , void const   *src , size_t n ) ;
#line 46 "../versisec/bind/progs1/../bind.h"
extern int dn_expand(u_char const   *msg , u_char const   *eomorig , u_char const   *comp_dn ,
                     char *exp_dn , int length ) ;
#line 50
extern int __VERIFIER_nondet_int() ;
extern int __VERIFIER_nondet_short() ;
#line 71 "/usr/include/assert.h"
extern  __attribute__((__nothrow__, __noreturn__)) void __assert_fail(char const   *__assertion ,
                                                                      char const   *__file ,
                                                                      unsigned int __line ,
                                                                      char const   *__function ) ;
#line 6 "../versisec/bind/progs1/bindCA-1999-14rrextract-nxt_two_expands_bad.c"
static int rrextract(u_char *msg , int msglen , u_char *rrp , u_char *dname , int namelen ) 
{ u_char *eom ;
  u_char *cp ;
  u_char *cp1 ;
  u_int dlen ;
  int n ;
  u_char data[6] ;
  int tmp ;
  unsigned int tmp___0 ;
  unsigned int tmp___1 ;
  unsigned long __cil_tmp18 ;
  unsigned long __cil_tmp19 ;
  unsigned long __cil_tmp20 ;
  u_char *__cil_tmp21 ;
  unsigned long __cil_tmp22 ;
  u_char const   *__cil_tmp23 ;
  u_char const   *__cil_tmp24 ;
  u_char const   *__cil_tmp25 ;
  u_char const   *__cil_tmp26 ;
  u_char const   *__cil_tmp27 ;
  u_char const   *__cil_tmp28 ;
  unsigned long __cil_tmp29 ;
  unsigned long __cil_tmp30 ;
  u_char *__cil_tmp31 ;
  int __cil_tmp32 ;
  unsigned long __cil_tmp33 ;
  unsigned long __cil_tmp34 ;
  u_char *__cil_tmp35 ;
  char const   *__cil_tmp36 ;
  unsigned long __cil_tmp37 ;
  unsigned long __cil_tmp38 ;
  u_char *__cil_tmp39 ;
  u_char *__cil_tmp40 ;
  unsigned long __cil_tmp41 ;
  unsigned long __cil_tmp42 ;
  u_char *__cil_tmp43 ;
  char const   *__cil_tmp44 ;
  unsigned int __cil_tmp45 ;
  unsigned long __cil_tmp46 ;
  unsigned long __cil_tmp47 ;
  u_int __cil_tmp48 ;
  unsigned long __cil_tmp49 ;
  void *__cil_tmp50 ;
  void const   *__cil_tmp51 ;
  u_int __cil_tmp52 ;

  {
#line 13
  __cil_tmp18 = 5 * 1UL;
#line 13
  __cil_tmp19 = (unsigned long )(data) + __cil_tmp18;
#line 13
  *((u_char *)__cil_tmp19) = (char)0;
#line 15
  cp = rrp;
#line 16
  eom = msg + msglen;
  {
#line 18
  while (1) {
    while_0_continue: /* CIL Label */ ;
    {
#line 18
    dlen = __VERIFIER_nondet_short();
#line 18
    cp = cp + 2;
    }
    goto while_0_break;
  }
  while_0_break: /* CIL Label */ ;
  }
  {
#line 19
  while (1) {
    while_1_continue: /* CIL Label */ ;
    {
#line 19
    __cil_tmp20 = (unsigned long )eom;
#line 19
    __cil_tmp21 = cp + dlen;
#line 19
    __cil_tmp22 = (unsigned long )__cil_tmp21;
#line 19
    if (__cil_tmp22 > __cil_tmp20) {
#line 19
      return (-1);
    } else {

    }
    }
    goto while_1_break;
  }
  while_1_break: /* CIL Label */ ;
  }
  {
#line 21
  __cil_tmp23 = (u_char const   *)msg;
#line 21
  __cil_tmp24 = (u_char const   *)eom;
#line 21
  __cil_tmp25 = (u_char const   *)cp;
#line 21
  n = dn_expand(__cil_tmp23, __cil_tmp24, __cil_tmp25, dname, namelen);
  }
#line 21
  if (n < 0) {
#line 22
    return (-1);
  } else {

  }
  {
#line 25
  cp = cp + n;
#line 29
  __cil_tmp26 = (u_char const   *)msg;
#line 29
  __cil_tmp27 = (u_char const   *)eom;
#line 29
  __cil_tmp28 = (u_char const   *)cp;
#line 29
  __cil_tmp29 = 0 * 1UL;
#line 29
  __cil_tmp30 = (unsigned long )(data) + __cil_tmp29;
#line 29
  __cil_tmp31 = (u_char *)__cil_tmp30;
#line 29
  __cil_tmp32 = (int )6UL;
#line 29
  n = dn_expand(__cil_tmp26, __cil_tmp27, __cil_tmp28, __cil_tmp31, __cil_tmp32);
  }
#line 31
  if (n < 0) {
#line 32
    return (-1);
  } else {
#line 31
    if (n >= dlen) {
#line 32
      return (-1);
    } else {

    }
  }
  {
#line 35
  tmp = __VERIFIER_nondet_int();
  }
#line 35
  if (tmp) {
#line 36
    return (-1);
  } else {

  }
  {
#line 38
  cp = cp + n;
#line 39
  __cil_tmp33 = 0 * 1UL;
#line 39
  __cil_tmp34 = (unsigned long )(data) + __cil_tmp33;
#line 39
  __cil_tmp35 = (u_char *)__cil_tmp34;
#line 39
  __cil_tmp36 = (char const   *)__cil_tmp35;
#line 39
  tmp___0 = strlen(__cil_tmp36);
#line 39
  __cil_tmp37 = 0 * 1UL;
#line 39
  __cil_tmp38 = (unsigned long )(data) + __cil_tmp37;
#line 39
  __cil_tmp39 = (u_char *)__cil_tmp38;
#line 39
  __cil_tmp40 = __cil_tmp39 + tmp___0;
#line 39
  cp1 = __cil_tmp40 + 1;
#line 42
  __cil_tmp41 = 0 * 1UL;
#line 42
  __cil_tmp42 = (unsigned long )(data) + __cil_tmp41;
#line 42
  __cil_tmp43 = (u_char *)__cil_tmp42;
#line 42
  __cil_tmp44 = (char const   *)__cil_tmp43;
#line 42
  tmp___1 = strlen(__cil_tmp44);
  }
  {
#line 42
  __cil_tmp45 = tmp___1 + 1U;
#line 42
  __cil_tmp46 = (unsigned long )__cil_tmp45;
#line 42
  __cil_tmp47 = 6UL - __cil_tmp46;
#line 42
  __cil_tmp48 = dlen - n;
#line 42
  __cil_tmp49 = (unsigned long )__cil_tmp48;
#line 42
  if (__cil_tmp49 <= __cil_tmp47) {

  } else {
    {
#line 42
    __assert_fail("dlen - n <= sizeof data - (strlen((char *)data) + 1)", "../versisec/bind/progs1/bindCA-1999-14rrextract-nxt_two_expands_bad.c",
                  42U, "rrextract");
    }
  }
  }
  {
#line 44
  __cil_tmp50 = (void *)cp1;
#line 44
  __cil_tmp51 = (void const   *)cp;
#line 44
  __cil_tmp52 = dlen - n;
#line 44
  r_memcpy(__cil_tmp50, __cil_tmp51, __cil_tmp52);
  }
#line 46
  return (0);
}
}
#line 49 "../versisec/bind/progs1/bindCA-1999-14rrextract-nxt_two_expands_bad.c"
int main(void) 
{ int msglen ;
  int ret ;
  u_char *dp ;
  u_char name[3] ;
  u_char msg[6] ;
  unsigned long __cil_tmp6 ;
  unsigned long __cil_tmp7 ;
  unsigned long __cil_tmp8 ;
  unsigned long __cil_tmp9 ;
  unsigned long __cil_tmp10 ;
  unsigned long __cil_tmp11 ;
  unsigned long __cil_tmp12 ;
  unsigned long __cil_tmp13 ;
  u_char *__cil_tmp14 ;
  unsigned long __cil_tmp15 ;
  unsigned long __cil_tmp16 ;
  u_char *__cil_tmp17 ;

  {
  {
#line 56
  __cil_tmp6 = 2 * 1UL;
#line 56
  __cil_tmp7 = (unsigned long )(name) + __cil_tmp6;
#line 56
  *((u_char *)__cil_tmp7) = (char)0;
#line 57
  __cil_tmp8 = 5 * 1UL;
#line 57
  __cil_tmp9 = (unsigned long )(msg) + __cil_tmp8;
#line 57
  *((u_char *)__cil_tmp9) = (char)0;
#line 59
  msglen = 4;
#line 60
  __cil_tmp10 = 0 * 1UL;
#line 60
  __cil_tmp11 = (unsigned long )(msg) + __cil_tmp10;
#line 60
  dp = (u_char *)__cil_tmp11;
#line 62
  __cil_tmp12 = 0 * 1UL;
#line 62
  __cil_tmp13 = (unsigned long )(msg) + __cil_tmp12;
#line 62
  __cil_tmp14 = (u_char *)__cil_tmp13;
#line 62
  __cil_tmp15 = 0 * 1UL;
#line 62
  __cil_tmp16 = (unsigned long )(name) + __cil_tmp15;
#line 62
  __cil_tmp17 = (u_char *)__cil_tmp16;
#line 62
  ret = rrextract(__cil_tmp14, msglen, dp, __cil_tmp17, 3);
  }
#line 64
  return (0);
}
}
