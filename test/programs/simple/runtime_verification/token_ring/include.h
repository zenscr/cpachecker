void error_fn() {
	ERROR: goto ERROR;
}

int __MONITOR_START_TRANSITION = 0;
int __MONITOR_END_TRANSITION = 0;

#define __MONITOR_START_TRANSITION __MONITOR_START_TRANSITION = __MONITOR_START_TRANSITION
#define __MONITOR_END_TRANSITION __MONITOR_END_TRANSITION = __MONITOR_END_TRANSITION 