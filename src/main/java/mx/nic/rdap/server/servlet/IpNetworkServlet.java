package mx.nic.rdap.server.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import mx.nic.rdap.core.db.IpNetwork;
import mx.nic.rdap.db.exception.InvalidValueException;
import mx.nic.rdap.db.exception.RdapDataAccessException;
import mx.nic.rdap.db.service.DataAccessService;
import mx.nic.rdap.db.spi.IpNetworkDAO;
import mx.nic.rdap.server.DataAccessServlet;
import mx.nic.rdap.server.RdapResult;
import mx.nic.rdap.server.exception.MalformedRequestException;
import mx.nic.rdap.server.exception.RequestHandleException;
import mx.nic.rdap.server.result.IpResult;
import mx.nic.rdap.server.util.Util;

@WebServlet(name = "ip", urlPatterns = { "/ip/*" })
public class IpNetworkServlet extends DataAccessServlet<IpNetworkDAO> {

	private static final long serialVersionUID = 1L;

	@Override
	protected IpNetworkDAO initAccessDAO() throws RdapDataAccessException {
		return DataAccessService.getIpNetworkDAO();
	}

	@Override
	protected String getServedObjectName() {
		return "IP network";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mx.nic.rdap.server.RdapServlet#doRdapGet(javax.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	protected RdapResult doRdapDaGet(HttpServletRequest httpRequest, IpNetworkDAO dao)
			throws RequestHandleException, IOException, SQLException, RdapDataAccessException {
		IpRequest request = new IpRequest(Util.getRequestParams(httpRequest));

		IpNetwork ipNetwork = null;
		try {
			if (request.hasCidr()) {
				ipNetwork = dao.getByInetAddress(request.getIp(), request.getCidr());
			} else {
				ipNetwork = dao.getByInetAddress(request.getIp());
			}

		} catch (InvalidValueException e) {
			throw new MalformedRequestException(e.getMessage(), e);
		}

		if (ipNetwork == null) {
			return null;
		}

		return new IpResult(httpRequest.getHeader("Host"), httpRequest.getContextPath(), ipNetwork,
				Util.getUsername(httpRequest));
	}

	private class IpRequest {

		private String ip;
		private Integer cidr;

		public IpRequest(String[] params) {
			super();
			this.ip = params[0];

			if (params.length > 1) {
				cidr = Integer.parseInt(params[1]);
			}
		}

		public String getIp() {
			return ip;
		}

		public Integer getCidr() {
			return cidr;
		}

		public boolean hasCidr() {
			return cidr != null;
		}

	}

}
