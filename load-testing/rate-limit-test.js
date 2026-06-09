import http from 'k6/http';
import { check } from 'k6';

// This test hits the limited endpoint at 200 RPS.
// We expect ~100 to pass (2xx/3xx/4xx) and ~100 to be rejected strictly by the Gateway with 429.
export const options = {
  scenarios: {
    rate_limiting: {
      executor: 'constant-arrival-rate',
      rate: 200,
      timeUnit: '1s',
      duration: '20s',
      preAllocatedVUs: 50,
      maxVUs: 100,
    },
  },
};

export default function () {
  const res = http.get('http://localhost:8481/api/perf/limited/test');
  
  check(res, {
    'status is under 500 (passed gateway)': (r) => r.status < 500 && r.status !== 429,
    'status is 429 (rate limited)': (r) => r.status === 429,
  });
}
