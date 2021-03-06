/* Generated by CIL v. 1.3.7 */
/* print_CIL_Input is true */

#line 44 "../dma_example.h"
struct _control_block {
   unsigned long long in_addr ;
   unsigned long long out_addr ;
   unsigned int num_elements_per_spe ;
   unsigned int id ;
   unsigned int pad[2] ;
};
#line 44 "../dma_example.h"
typedef struct _control_block control_block_t;
#line 339 "/usr/include/stdio.h"
extern int printf(char const   * __restrict  __format  , ...) ;
#line 203 "../spu_mfcio.h"
extern void mfc_put(void volatile   *ls , unsigned int ea , unsigned int size , unsigned int tag ,
                    unsigned int tid , unsigned int rid ) ;
#line 211
extern void mfc_get(void volatile   *ls , unsigned int ea , unsigned int size , unsigned int tag ,
                    unsigned int tid , unsigned int rid ) ;
#line 252
extern void mfc_write_tag_mask(unsigned int mask ) ;
#line 270
extern void mfc_read_tag_status_all() ;
#line 24 "dma_example_spu.c"
float local_buffers[3][4096]  __attribute__((__aligned__(128)))  ;
#line 27 "dma_example_spu.c"
control_block_t control_block  __attribute__((__aligned__(128)))  ;
#line 77
extern int __VERIFIER_nondet_int() ;
#line 119
extern int ( /* missing proto */  assert)() ;
#line 48 "dma_example_spu.c"
int spu_main(unsigned long long speid  __attribute__((__unused__)) , unsigned long long argp ,
             unsigned long long envp  __attribute__((__unused__)) ) 
{ unsigned int tags[3] ;
  unsigned long long in_addr ;
  unsigned long long out_addr ;
  unsigned int i ;
  unsigned int num_chunks ;
  int put_buf ;
  int get_buf ;
  int process_buf ;
  int tmp ;
  int tmp___0 ;
  int tmp___1 ;
  int tmp___2 ;
  int tmp___3 ;
  int tmp___4 ;
  int tmp___5 ;
  int tmp___6 ;
  int tmp___7 ;
  int tmp___8 ;
  unsigned long __cil_tmp22 ;
  unsigned long __cil_tmp23 ;
  unsigned long __cil_tmp24 ;
  unsigned long __cil_tmp25 ;
  unsigned long __cil_tmp26 ;
  unsigned long __cil_tmp27 ;
  unsigned long __cil_tmp28 ;
  unsigned long __cil_tmp29 ;
  unsigned int __cil_tmp30 ;
  char const   * __restrict  __cil_tmp31 ;
  unsigned long __cil_tmp32 ;
  unsigned long __cil_tmp33 ;
  unsigned int __cil_tmp34 ;
  char const   * __restrict  __cil_tmp35 ;
  unsigned long __cil_tmp36 ;
  unsigned long __cil_tmp37 ;
  unsigned int __cil_tmp38 ;
  char const   * __restrict  __cil_tmp39 ;
  void volatile   *__cil_tmp40 ;
  unsigned int __cil_tmp41 ;
  unsigned int __cil_tmp42 ;
  unsigned long __cil_tmp43 ;
  unsigned long __cil_tmp44 ;
  unsigned int __cil_tmp45 ;
  control_block_t *__cil_tmp46 ;
  unsigned long __cil_tmp47 ;
  unsigned long __cil_tmp48 ;
  unsigned long __cil_tmp49 ;
  unsigned long __cil_tmp50 ;
  unsigned long __cil_tmp51 ;
  unsigned long __cil_tmp52 ;
  unsigned long __cil_tmp53 ;
  unsigned long __cil_tmp54 ;
  unsigned long __cil_tmp55 ;
  unsigned long __cil_tmp56 ;
  unsigned long __cil_tmp57 ;
  unsigned int __cil_tmp58 ;
  int __cil_tmp59 ;
  unsigned int __cil_tmp60 ;
  unsigned long __cil_tmp61 ;
  unsigned int __cil_tmp62 ;
  control_block_t *__cil_tmp63 ;
  unsigned long __cil_tmp64 ;
  unsigned long __cil_tmp65 ;
  unsigned long __cil_tmp66 ;
  unsigned long __cil_tmp67 ;
  unsigned long __cil_tmp68 ;
  float *__cil_tmp69 ;
  void volatile   *__cil_tmp70 ;
  unsigned int __cil_tmp71 ;
  unsigned long __cil_tmp72 ;
  unsigned int __cil_tmp73 ;
  unsigned long __cil_tmp74 ;
  unsigned long __cil_tmp75 ;
  unsigned int __cil_tmp76 ;
  unsigned long __cil_tmp77 ;
  unsigned long long __cil_tmp78 ;
  unsigned long __cil_tmp79 ;
  unsigned long __cil_tmp80 ;
  unsigned long __cil_tmp81 ;
  unsigned long __cil_tmp82 ;
  float *__cil_tmp83 ;
  void volatile   *__cil_tmp84 ;
  unsigned int __cil_tmp85 ;
  unsigned long __cil_tmp86 ;
  unsigned int __cil_tmp87 ;
  unsigned long __cil_tmp88 ;
  unsigned long __cil_tmp89 ;
  unsigned int __cil_tmp90 ;
  unsigned long __cil_tmp91 ;
  unsigned long long __cil_tmp92 ;
  unsigned long __cil_tmp93 ;
  unsigned long __cil_tmp94 ;
  unsigned int __cil_tmp95 ;
  int __cil_tmp96 ;
  unsigned int __cil_tmp97 ;
  unsigned long __cil_tmp98 ;
  unsigned long __cil_tmp99 ;
  unsigned int __cil_tmp100 ;
  int __cil_tmp101 ;
  unsigned long __cil_tmp102 ;
  unsigned long __cil_tmp103 ;
  unsigned int __cil_tmp104 ;
  int __cil_tmp105 ;
  unsigned long __cil_tmp106 ;
  unsigned long __cil_tmp107 ;
  unsigned int __cil_tmp108 ;
  int __cil_tmp109 ;
  unsigned long __cil_tmp110 ;
  unsigned long __cil_tmp111 ;
  unsigned long __cil_tmp112 ;
  unsigned long __cil_tmp113 ;
  float *__cil_tmp114 ;
  void volatile   *__cil_tmp115 ;
  unsigned int __cil_tmp116 ;
  unsigned long __cil_tmp117 ;
  unsigned int __cil_tmp118 ;
  unsigned long __cil_tmp119 ;
  unsigned long __cil_tmp120 ;
  unsigned int __cil_tmp121 ;
  unsigned long __cil_tmp122 ;
  unsigned long long __cil_tmp123 ;
  unsigned long __cil_tmp124 ;
  unsigned long __cil_tmp125 ;
  unsigned int __cil_tmp126 ;
  int __cil_tmp127 ;
  unsigned int __cil_tmp128 ;
  unsigned long __cil_tmp129 ;
  unsigned long __cil_tmp130 ;
  unsigned long __cil_tmp131 ;
  unsigned long __cil_tmp132 ;
  float *__cil_tmp133 ;
  void volatile   *__cil_tmp134 ;
  unsigned int __cil_tmp135 ;
  unsigned long __cil_tmp136 ;
  unsigned int __cil_tmp137 ;
  unsigned long __cil_tmp138 ;
  unsigned long __cil_tmp139 ;
  unsigned int __cil_tmp140 ;
  unsigned long __cil_tmp141 ;
  unsigned long long __cil_tmp142 ;
  unsigned long __cil_tmp143 ;
  unsigned long __cil_tmp144 ;
  unsigned int __cil_tmp145 ;
  int __cil_tmp146 ;
  unsigned int __cil_tmp147 ;
  unsigned long __cil_tmp148 ;
  unsigned long __cil_tmp149 ;
  unsigned long __cil_tmp150 ;
  unsigned long __cil_tmp151 ;
  float *__cil_tmp152 ;
  void volatile   *__cil_tmp153 ;
  unsigned int __cil_tmp154 ;
  unsigned long __cil_tmp155 ;
  unsigned int __cil_tmp156 ;
  unsigned long __cil_tmp157 ;
  unsigned long __cil_tmp158 ;
  unsigned int __cil_tmp159 ;
  unsigned long __cil_tmp160 ;
  unsigned long long __cil_tmp161 ;
  unsigned long __cil_tmp162 ;
  unsigned long __cil_tmp163 ;
  unsigned int __cil_tmp164 ;
  int __cil_tmp165 ;
  unsigned int __cil_tmp166 ;
  unsigned long __cil_tmp167 ;
  unsigned long __cil_tmp168 ;
  unsigned long __cil_tmp169 ;
  unsigned long __cil_tmp170 ;
  float *__cil_tmp171 ;
  void volatile   *__cil_tmp172 ;
  unsigned int __cil_tmp173 ;
  unsigned long __cil_tmp174 ;
  unsigned int __cil_tmp175 ;
  unsigned long __cil_tmp176 ;
  unsigned long __cil_tmp177 ;
  unsigned int __cil_tmp178 ;
  unsigned long __cil_tmp179 ;
  unsigned long __cil_tmp180 ;
  unsigned int __cil_tmp181 ;
  int __cil_tmp182 ;
  unsigned int __cil_tmp183 ;

  {
#line 66
  __cil_tmp22 = 0 * 4UL;
#line 66
  __cil_tmp23 = (unsigned long )(tags) + __cil_tmp22;
#line 66
  *((unsigned int *)__cil_tmp23) = 0U;
#line 67
  __cil_tmp24 = 1 * 4UL;
#line 67
  __cil_tmp25 = (unsigned long )(tags) + __cil_tmp24;
#line 67
  *((unsigned int *)__cil_tmp25) = 1U;
#line 68
  __cil_tmp26 = 2 * 4UL;
#line 68
  __cil_tmp27 = (unsigned long )(tags) + __cil_tmp26;
#line 68
  *((unsigned int *)__cil_tmp27) = 2U;
  {
#line 70
  __cil_tmp28 = 0 * 4UL;
#line 70
  __cil_tmp29 = (unsigned long )(tags) + __cil_tmp28;
#line 70
  __cil_tmp30 = *((unsigned int *)__cil_tmp29);
#line 70
  if (__cil_tmp30 == 4294967295U) {
    {
#line 72
    __cil_tmp31 = (char const   * __restrict  )"SPU ERROR, unable to reserve tag\n";
#line 72
    printf(__cil_tmp31);
    }
#line 73
    return (1);
  } else {
    {
#line 70
    __cil_tmp32 = 1 * 4UL;
#line 70
    __cil_tmp33 = (unsigned long )(tags) + __cil_tmp32;
#line 70
    __cil_tmp34 = *((unsigned int *)__cil_tmp33);
#line 70
    if (__cil_tmp34 == 4294967295U) {
      {
#line 72
      __cil_tmp35 = (char const   * __restrict  )"SPU ERROR, unable to reserve tag\n";
#line 72
      printf(__cil_tmp35);
      }
#line 73
      return (1);
    } else {
      {
#line 70
      __cil_tmp36 = 2 * 4UL;
#line 70
      __cil_tmp37 = (unsigned long )(tags) + __cil_tmp36;
#line 70
      __cil_tmp38 = *((unsigned int *)__cil_tmp37);
#line 70
      if (__cil_tmp38 == 4294967295U) {
        {
#line 72
        __cil_tmp39 = (char const   * __restrict  )"SPU ERROR, unable to reserve tag\n";
#line 72
        printf(__cil_tmp39);
        }
#line 73
        return (1);
      } else {

      }
      }
    }
    }
  }
  }
  {
#line 76
  __cil_tmp40 = (void volatile   *)(& control_block);
#line 76
  __cil_tmp41 = (unsigned int )argp;
#line 76
  __cil_tmp42 = (unsigned int )32UL;
#line 76
  __cil_tmp43 = 0 * 4UL;
#line 76
  __cil_tmp44 = (unsigned long )(tags) + __cil_tmp43;
#line 76
  __cil_tmp45 = *((unsigned int *)__cil_tmp44);
#line 76
  mfc_get(__cil_tmp40, __cil_tmp41, __cil_tmp42, __cil_tmp45, 0U, 0U);
#line 77
  tmp = __VERIFIER_nondet_int();
#line 77
  __cil_tmp46 = & control_block;
#line 77
  *((unsigned long long *)__cil_tmp46) = (unsigned long long )tmp;
#line 77
  tmp___0 = __VERIFIER_nondet_int();
#line 77
  __cil_tmp47 = (unsigned long )(& control_block) + 8;
#line 77
  *((unsigned long long *)__cil_tmp47) = (unsigned long long )tmp___0;
#line 77
  tmp___1 = __VERIFIER_nondet_int();
#line 77
  __cil_tmp48 = (unsigned long )(& control_block) + 16;
#line 77
  *((unsigned int *)__cil_tmp48) = (unsigned int )tmp___1;
#line 77
  tmp___2 = __VERIFIER_nondet_int();
#line 77
  __cil_tmp49 = (unsigned long )(& control_block) + 20;
#line 77
  *((unsigned int *)__cil_tmp49) = (unsigned int )tmp___2;
#line 77
  tmp___3 = __VERIFIER_nondet_int();
#line 77
  __cil_tmp50 = 0 * 4UL;
#line 77
  __cil_tmp51 = 24 + __cil_tmp50;
#line 77
  __cil_tmp52 = (unsigned long )(& control_block) + __cil_tmp51;
#line 77
  *((unsigned int *)__cil_tmp52) = (unsigned int )tmp___3;
#line 77
  tmp___4 = __VERIFIER_nondet_int();
#line 77
  __cil_tmp53 = 1 * 4UL;
#line 77
  __cil_tmp54 = 24 + __cil_tmp53;
#line 77
  __cil_tmp55 = (unsigned long )(& control_block) + __cil_tmp54;
#line 77
  *((unsigned int *)__cil_tmp55) = (unsigned int )tmp___4;
#line 78
  __cil_tmp56 = 0 * 4UL;
#line 78
  __cil_tmp57 = (unsigned long )(tags) + __cil_tmp56;
#line 78
  __cil_tmp58 = *((unsigned int *)__cil_tmp57);
#line 78
  __cil_tmp59 = 1 << __cil_tmp58;
#line 78
  __cil_tmp60 = (unsigned int )__cil_tmp59;
#line 78
  mfc_write_tag_mask(__cil_tmp60);
#line 79
  mfc_read_tag_status_all();
#line 81
  __cil_tmp61 = (unsigned long )(& control_block) + 16;
#line 81
  __cil_tmp62 = *((unsigned int *)__cil_tmp61);
#line 81
  num_chunks = __cil_tmp62 / 4096U;
#line 84
  __cil_tmp63 = & control_block;
#line 84
  in_addr = *((unsigned long long *)__cil_tmp63);
#line 85
  __cil_tmp64 = (unsigned long )(& control_block) + 8;
#line 85
  out_addr = *((unsigned long long *)__cil_tmp64);
#line 88
  __cil_tmp65 = 0 * 4UL;
#line 88
  __cil_tmp66 = 0 * 16384UL;
#line 88
  __cil_tmp67 = __cil_tmp66 + __cil_tmp65;
#line 88
  __cil_tmp68 = (unsigned long )(local_buffers) + __cil_tmp67;
#line 88
  __cil_tmp69 = (float *)__cil_tmp68;
#line 88
  __cil_tmp70 = (void volatile   *)__cil_tmp69;
#line 88
  __cil_tmp71 = (unsigned int )in_addr;
#line 88
  __cil_tmp72 = 4096UL * 4UL;
#line 88
  __cil_tmp73 = (unsigned int )__cil_tmp72;
#line 88
  __cil_tmp74 = 0 * 4UL;
#line 88
  __cil_tmp75 = (unsigned long )(tags) + __cil_tmp74;
#line 88
  __cil_tmp76 = *((unsigned int *)__cil_tmp75);
#line 88
  mfc_get(__cil_tmp70, __cil_tmp71, __cil_tmp73, __cil_tmp76, 0U, 0U);
#line 92
  __cil_tmp77 = 4096UL * 4UL;
#line 92
  __cil_tmp78 = (unsigned long long )__cil_tmp77;
#line 92
  in_addr = in_addr + __cil_tmp78;
#line 95
  __cil_tmp79 = 0 * 4UL;
#line 95
  __cil_tmp80 = 1 * 16384UL;
#line 95
  __cil_tmp81 = __cil_tmp80 + __cil_tmp79;
#line 95
  __cil_tmp82 = (unsigned long )(local_buffers) + __cil_tmp81;
#line 95
  __cil_tmp83 = (float *)__cil_tmp82;
#line 95
  __cil_tmp84 = (void volatile   *)__cil_tmp83;
#line 95
  __cil_tmp85 = (unsigned int )in_addr;
#line 95
  __cil_tmp86 = 4096UL * 4UL;
#line 95
  __cil_tmp87 = (unsigned int )__cil_tmp86;
#line 95
  __cil_tmp88 = 1 * 4UL;
#line 95
  __cil_tmp89 = (unsigned long )(tags) + __cil_tmp88;
#line 95
  __cil_tmp90 = *((unsigned int *)__cil_tmp89);
#line 95
  mfc_get(__cil_tmp84, __cil_tmp85, __cil_tmp87, __cil_tmp90, 0U, 0U);
#line 98
  __cil_tmp91 = 4096UL * 4UL;
#line 98
  __cil_tmp92 = (unsigned long long )__cil_tmp91;
#line 98
  in_addr = in_addr + __cil_tmp92;
#line 101
  __cil_tmp93 = 0 * 4UL;
#line 101
  __cil_tmp94 = (unsigned long )(tags) + __cil_tmp93;
#line 101
  __cil_tmp95 = *((unsigned int *)__cil_tmp94);
#line 101
  __cil_tmp96 = 1 << __cil_tmp95;
#line 101
  __cil_tmp97 = (unsigned int )__cil_tmp96;
#line 101
  mfc_write_tag_mask(__cil_tmp97);
#line 102
  mfc_read_tag_status_all();
#line 110
  put_buf = 0;
#line 111
  process_buf = 1;
#line 112
  get_buf = 2;
#line 114
  i = 2U;
  }
  {
#line 114
  while (1) {
    while_continue: /* CIL Label */ ;
#line 114
    if (i < num_chunks) {

    } else {
#line 114
      goto while_break;
    }
#line 119
    if (put_buf >= 0) {
#line 119
      if (put_buf < 3) {
#line 119
        tmp___6 = 1;
      } else {
#line 119
        tmp___6 = 0;
      }
    } else {
#line 119
      tmp___6 = 0;
    }
    {
#line 119
    assert(tmp___6);
    }
#line 120
    if (get_buf >= 0) {
#line 120
      if (get_buf < 3) {
#line 120
        tmp___7 = 1;
      } else {
#line 120
        tmp___7 = 0;
      }
    } else {
#line 120
      tmp___7 = 0;
    }
    {
#line 120
    assert(tmp___7);
    }
#line 121
    if (process_buf >= 0) {
#line 121
      if (process_buf < 3) {
#line 121
        tmp___8 = 1;
      } else {
#line 121
        tmp___8 = 0;
      }
    } else {
#line 121
      tmp___8 = 0;
    }
    {
#line 121
    assert(tmp___8);
#line 122
    __cil_tmp98 = 0 * 4UL;
#line 122
    __cil_tmp99 = (unsigned long )(tags) + __cil_tmp98;
#line 122
    __cil_tmp100 = *((unsigned int *)__cil_tmp99);
#line 122
    __cil_tmp101 = __cil_tmp100 == 0U;
#line 122
    assert(__cil_tmp101);
#line 123
    __cil_tmp102 = 1 * 4UL;
#line 123
    __cil_tmp103 = (unsigned long )(tags) + __cil_tmp102;
#line 123
    __cil_tmp104 = *((unsigned int *)__cil_tmp103);
#line 123
    __cil_tmp105 = __cil_tmp104 == 1U;
#line 123
    assert(__cil_tmp105);
#line 124
    __cil_tmp106 = 2 * 4UL;
#line 124
    __cil_tmp107 = (unsigned long )(tags) + __cil_tmp106;
#line 124
    __cil_tmp108 = *((unsigned int *)__cil_tmp107);
#line 124
    __cil_tmp109 = __cil_tmp108 == 2U;
#line 124
    assert(__cil_tmp109);
#line 127
    __cil_tmp110 = 0 * 4UL;
#line 127
    __cil_tmp111 = put_buf * 16384UL;
#line 127
    __cil_tmp112 = __cil_tmp111 + __cil_tmp110;
#line 127
    __cil_tmp113 = (unsigned long )(local_buffers) + __cil_tmp112;
#line 127
    __cil_tmp114 = (float *)__cil_tmp113;
#line 127
    __cil_tmp115 = (void volatile   *)__cil_tmp114;
#line 127
    __cil_tmp116 = (unsigned int )out_addr;
#line 127
    __cil_tmp117 = 4096UL * 4UL;
#line 127
    __cil_tmp118 = (unsigned int )__cil_tmp117;
#line 127
    __cil_tmp119 = put_buf * 4UL;
#line 127
    __cil_tmp120 = (unsigned long )(tags) + __cil_tmp119;
#line 127
    __cil_tmp121 = *((unsigned int *)__cil_tmp120);
#line 127
    mfc_put(__cil_tmp115, __cil_tmp116, __cil_tmp118, __cil_tmp121, 0U, 0U);
#line 131
    __cil_tmp122 = 4096UL * 4UL;
#line 131
    __cil_tmp123 = (unsigned long long )__cil_tmp122;
#line 131
    out_addr = out_addr + __cil_tmp123;
#line 134
    __cil_tmp124 = get_buf * 4UL;
#line 134
    __cil_tmp125 = (unsigned long )(tags) + __cil_tmp124;
#line 134
    __cil_tmp126 = *((unsigned int *)__cil_tmp125);
#line 134
    __cil_tmp127 = 1 << __cil_tmp126;
#line 134
    __cil_tmp128 = (unsigned int )__cil_tmp127;
#line 134
    mfc_write_tag_mask(__cil_tmp128);
#line 135
    mfc_read_tag_status_all();
#line 136
    __cil_tmp129 = 0 * 4UL;
#line 136
    __cil_tmp130 = get_buf * 16384UL;
#line 136
    __cil_tmp131 = __cil_tmp130 + __cil_tmp129;
#line 136
    __cil_tmp132 = (unsigned long )(local_buffers) + __cil_tmp131;
#line 136
    __cil_tmp133 = (float *)__cil_tmp132;
#line 136
    __cil_tmp134 = (void volatile   *)__cil_tmp133;
#line 136
    __cil_tmp135 = (unsigned int )in_addr;
#line 136
    __cil_tmp136 = 4096UL * 4UL;
#line 136
    __cil_tmp137 = (unsigned int )__cil_tmp136;
#line 136
    __cil_tmp138 = get_buf * 4UL;
#line 136
    __cil_tmp139 = (unsigned long )(tags) + __cil_tmp138;
#line 136
    __cil_tmp140 = *((unsigned int *)__cil_tmp139);
#line 136
    mfc_get(__cil_tmp134, __cil_tmp135, __cil_tmp137, __cil_tmp140, 0U, 0U);
#line 139
    __cil_tmp141 = 4096UL * 4UL;
#line 139
    __cil_tmp142 = (unsigned long long )__cil_tmp141;
#line 139
    in_addr = in_addr + __cil_tmp142;
#line 142
    __cil_tmp143 = process_buf * 4UL;
#line 142
    __cil_tmp144 = (unsigned long )(tags) + __cil_tmp143;
#line 142
    __cil_tmp145 = *((unsigned int *)__cil_tmp144);
#line 142
    __cil_tmp146 = 1 << __cil_tmp145;
#line 142
    __cil_tmp147 = (unsigned int )__cil_tmp146;
#line 142
    mfc_write_tag_mask(__cil_tmp147);
#line 155
    tmp___5 = put_buf;
#line 156
    put_buf = process_buf;
#line 157
    process_buf = get_buf;
#line 158
    get_buf = tmp___5;
#line 114
    i = i + 1U;
    }
  }
  while_break: /* CIL Label */ ;
  }
  {
#line 162
  __cil_tmp148 = 0 * 4UL;
#line 162
  __cil_tmp149 = put_buf * 16384UL;
#line 162
  __cil_tmp150 = __cil_tmp149 + __cil_tmp148;
#line 162
  __cil_tmp151 = (unsigned long )(local_buffers) + __cil_tmp150;
#line 162
  __cil_tmp152 = (float *)__cil_tmp151;
#line 162
  __cil_tmp153 = (void volatile   *)__cil_tmp152;
#line 162
  __cil_tmp154 = (unsigned int )out_addr;
#line 162
  __cil_tmp155 = 4096UL * 4UL;
#line 162
  __cil_tmp156 = (unsigned int )__cil_tmp155;
#line 162
  __cil_tmp157 = put_buf * 4UL;
#line 162
  __cil_tmp158 = (unsigned long )(tags) + __cil_tmp157;
#line 162
  __cil_tmp159 = *((unsigned int *)__cil_tmp158);
#line 162
  mfc_put(__cil_tmp153, __cil_tmp154, __cil_tmp156, __cil_tmp159, 0U, 0U);
#line 165
  __cil_tmp160 = 4096UL * 4UL;
#line 165
  __cil_tmp161 = (unsigned long long )__cil_tmp160;
#line 165
  out_addr = out_addr + __cil_tmp161;
#line 168
  __cil_tmp162 = process_buf * 4UL;
#line 168
  __cil_tmp163 = (unsigned long )(tags) + __cil_tmp162;
#line 168
  __cil_tmp164 = *((unsigned int *)__cil_tmp163);
#line 168
  __cil_tmp165 = 1 << __cil_tmp164;
#line 168
  __cil_tmp166 = (unsigned int )__cil_tmp165;
#line 168
  mfc_write_tag_mask(__cil_tmp166);
#line 169
  mfc_read_tag_status_all();
#line 176
  put_buf = process_buf;
#line 179
  __cil_tmp167 = 0 * 4UL;
#line 179
  __cil_tmp168 = put_buf * 16384UL;
#line 179
  __cil_tmp169 = __cil_tmp168 + __cil_tmp167;
#line 179
  __cil_tmp170 = (unsigned long )(local_buffers) + __cil_tmp169;
#line 179
  __cil_tmp171 = (float *)__cil_tmp170;
#line 179
  __cil_tmp172 = (void volatile   *)__cil_tmp171;
#line 179
  __cil_tmp173 = (unsigned int )out_addr;
#line 179
  __cil_tmp174 = 4096UL * 4UL;
#line 179
  __cil_tmp175 = (unsigned int )__cil_tmp174;
#line 179
  __cil_tmp176 = put_buf * 4UL;
#line 179
  __cil_tmp177 = (unsigned long )(tags) + __cil_tmp176;
#line 179
  __cil_tmp178 = *((unsigned int *)__cil_tmp177);
#line 179
  mfc_put(__cil_tmp172, __cil_tmp173, __cil_tmp175, __cil_tmp178, 0U, 0U);
#line 183
  __cil_tmp179 = put_buf * 4UL;
#line 183
  __cil_tmp180 = (unsigned long )(tags) + __cil_tmp179;
#line 183
  __cil_tmp181 = *((unsigned int *)__cil_tmp180);
#line 183
  __cil_tmp182 = 1 << __cil_tmp181;
#line 183
  __cil_tmp183 = (unsigned int )__cil_tmp182;
#line 183
  mfc_write_tag_mask(__cil_tmp183);
#line 184
  mfc_read_tag_status_all();
  }
#line 192
  return (0);
}
}
