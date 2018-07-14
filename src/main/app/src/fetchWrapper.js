/**
 * Extends a standard {@link Error} by capturing the fetch {@link Response} as the <code>response</code> property
 */
export class FetchError  {
  /**
   * The fetch response
   * @type {Response}
   */
  response = null;

  constructor(response) {
    this.response = response;
  }
}

/**
 * Wraps a standard fetch call with the typical options needed to convey authenticated session and intercepts
 * non-ok responses and throws a {@link FetchError}
 * @param {string} url the URL or path to fetch
 * @param {object} options the additional options to pass to the fetch call
 * @returns {Promise<Response>} the fetch's promise
 */
export default function (url, options = {}) {
  const {headers, ...remainder} = options;

  const withDefaults = {
    credentials: "same-origin",
    headers: {
      "Accept": "application/json",
      ...headers
    },
    ...remainder
  };

  return fetch(url, withDefaults)
    .then(response => {
      if (!response.ok) {
        throw new FetchError(response);
      }
      return response;
    })
};