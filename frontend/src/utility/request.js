export const GET = "get";
export const POST = "post";
export const PUT = "put";
export const DELETE = "delete";

export const baseURL = "http://localhost:8080";

import axios from "axios";
export const request = async (url, method, data) => {
  // Check if given a valid request method.
  if ([GET, POST, PUT, DELETE].includes(method)) {
    if (method === GET) {
      // Get request doesn't include data.
      data = "";
    }

    return await axios({
      method: method,
      headers: { "content-type": "application/json" },
      url: url,
      withCredentials: true,
      data: data,
    });
  }

  console.error(`Invalid request method: ${method}!`);
  return { status: 500 };
}
