import http from 'k6/http';
import { check } from 'k6';

// This test evaluates the raw throughput the Spring Cloud Gateway can sustain
// when forwarding to a downstream microservice.
export const options = {
  stages: [
    { duration: '5s', target: 50 },
    { duration: '20s', target: 50 },
    { duration: '5s', target: 0 },
  ],
};

export default function () {
  const res = http.get('http://localhost:8481/api/perf/unlimited/test');
  
  // We check for any response that comes from the backend (2xx, 3xx, 4xx).
  // Even a 404 from the downstream service means the Gateway routed it successfully.
  check(res, {
    'status is not 502/503/504': (r) => r.status < 500,
  });
}
