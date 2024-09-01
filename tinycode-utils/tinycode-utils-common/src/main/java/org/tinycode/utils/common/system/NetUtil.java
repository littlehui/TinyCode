package org.tinycode.utils.common.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinycode.utils.common.collector.CollectionUtils;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class NetUtil {
    private static Logger logger = LoggerFactory.getLogger(NetUtil.class);
    private static final Pattern ADDRESS_PATTERN;
    private static final Pattern LOCAL_IP_PATTERN;
    private static final Pattern IP_PATTERN;
    private static volatile InetAddress LOCAL_ADDRESS;
    private static volatile String HOST_ADDRESS;
    static {
        ADDRESS_PATTERN = Pattern.compile("^\\d{1,3}(\\.\\d{1,3}){3}\\:\\d{1,5}$");
        LOCAL_IP_PATTERN = Pattern.compile("127(\\.\\d{1,3}){3}$");
        IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");
        LOCAL_ADDRESS = null;
    }

    public NetUtil() {
    }

    public static int getRandomPort() {
        return 30000 + ThreadLocalRandom.current().nextInt(10000);
    }

    public static int getAvailablePort() {
        try {
            ServerSocket ss = new ServerSocket();
            Throwable var1 = null;

            int var2;
            try {
                ss.bind((SocketAddress)null);
                var2 = ss.getLocalPort();
            } catch (Throwable var12) {
                var1 = var12;
                throw var12;
            } finally {
                if (ss != null) {
                    if (var1 != null) {
                        try {
                            ss.close();
                        } catch (Throwable var11) {
                            var1.addSuppressed(var11);
                        }
                    } else {
                        ss.close();
                    }
                }

            }

            return var2;
        } catch (IOException var14) {
            return getRandomPort();
        }
    }

    public static int getAvailablePort(int port) {
        if (port <= 0) {
            return getAvailablePort();
        } else {
            int i = port;

            while(i < 65535) {
                try {
                    ServerSocket ignored = new ServerSocket(i);
                    Throwable var3 = null;

                    int var4;
                    try {
                        var4 = i;
                    } catch (Throwable var14) {
                        var3 = var14;
                        throw var14;
                    } finally {
                        if (ignored != null) {
                            if (var3 != null) {
                                try {
                                    ignored.close();
                                } catch (Throwable var13) {
                                    var3.addSuppressed(var13);
                                }
                            } else {
                                ignored.close();
                            }
                        }

                    }

                    return var4;
                } catch (IOException var16) {
                    ++i;
                }
            }

            return port;
        }
    }

    public static boolean isInvalidPort(int port) {
        return port <= 0 || port > 65535;
    }

    public static boolean isValidAddress(String address) {
        return ADDRESS_PATTERN.matcher(address).matches();
    }

    public static boolean isLocalHost(String host) {
        return host != null && (LOCAL_IP_PATTERN.matcher(host).matches() || host.equalsIgnoreCase("localhost"));
    }

    public static boolean isAnyHost(String host) {
        return "0.0.0.0".equals(host);
    }

    public static boolean isInvalidLocalHost(String host) {
        return host == null || host.length() == 0 || host.equalsIgnoreCase("localhost") || host.equals("0.0.0.0") || host.startsWith("127.");
    }

    public static boolean isValidLocalHost(String host) {
        return !isInvalidLocalHost(host);
    }

    public static InetSocketAddress getLocalSocketAddress(String host, int port) {
        return isInvalidLocalHost(host) ? new InetSocketAddress(port) : new InetSocketAddress(host, port);
    }

    static boolean isPreferIPV6Address() {
        return Boolean.getBoolean("java.net.preferIPv6Addresses");
    }

    static InetAddress normalizeV6Address(Inet6Address address) {
        String addr = address.getHostAddress();
        int i = addr.lastIndexOf(37);
        if (i > 0) {
            try {
                return InetAddress.getByName(addr.substring(0, i) + '%' + address.getScopeId());
            } catch (UnknownHostException var4) {
                logger.debug("Unknown IPV6 address: ", var4);
            }
        }

        return address;
    }

    public static String getLocalHost() {
        if (HOST_ADDRESS != null) {
            return HOST_ADDRESS;
        } else {
            InetAddress address = getLocalAddress();
            return address != null ? (HOST_ADDRESS = address.getHostAddress()) : "127.0.0.1";
        }
    }


    public static InetAddress getLocalAddress() {
        if (LOCAL_ADDRESS != null) {
            return LOCAL_ADDRESS;
        } else {
            InetAddress localAddress = getLocalAddress0();
            LOCAL_ADDRESS = localAddress;
            return localAddress;
        }
    }


    private static InetAddress getLocalAddress0() {
        InetAddress localAddress = null;

        try {
            NetworkInterface networkInterface = findNetworkInterface();
            Enumeration addresses = networkInterface.getInetAddresses();

            while(addresses.hasMoreElements()) {
                Optional<InetAddress> addressOp = toValidAddress((InetAddress)addresses.nextElement());
                if (addressOp.isPresent()) {
                    try {
                        if (((InetAddress)addressOp.get()).isReachable(100)) {
                            return (InetAddress)addressOp.get();
                        }
                    } catch (IOException var6) {
                    }
                }
            }
        } catch (Throwable var7) {
            logger.warn("Netutils warn", var7);
        }

        try {
            localAddress = InetAddress.getLocalHost();
            Optional<InetAddress> addressOp = toValidAddress(localAddress);
            if (addressOp.isPresent()) {
                return (InetAddress)addressOp.get();
            }
        } catch (Throwable var5) {
            logger.warn("Netutils warn",var5);
        }

        return localAddress;
    }


    public static NetworkInterface findNetworkInterface() {
        List validNetworkInterfaces = Collections.emptyList();

        try {
            validNetworkInterfaces = getValidNetworkInterfaces();
        } catch (Throwable var4) {
            logger.warn("Netutils warn", var4);
        }

        NetworkInterface result = null;
        Iterator var2 = validNetworkInterfaces.iterator();

        while(var2.hasNext()) {
            NetworkInterface networkInterface = (NetworkInterface)var2.next();
            if (isPreferredNetworkInterface(networkInterface)) {
                result = networkInterface;
                break;
            }
        }

        if (result == null) {
            result = (NetworkInterface) CollectionUtils.first(validNetworkInterfaces);
        }

        return result;
    }

    private static Optional<InetAddress> toValidAddress(InetAddress address) {
        if (address instanceof Inet6Address) {
            Inet6Address v6Address = (Inet6Address)address;
            if (isPreferIPV6Address()) {
                return Optional.ofNullable(normalizeV6Address(v6Address));
            }
        }

        return isValidV4Address(address) ? Optional.of(address) : Optional.empty();
    }


    static boolean isValidV4Address(InetAddress address) {
        if (address != null && !address.isLoopbackAddress()) {
            String name = address.getHostAddress();
            return name != null && IP_PATTERN.matcher(name).matches() && !"0.0.0.0".equals(name) && !"127.0.0.1".equals(name);
        } else {
            return false;
        }
    }
    private static boolean ignoreNetworkInterface(NetworkInterface networkInterface) throws SocketException {
        return networkInterface == null || networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp();
    }

    private static List<NetworkInterface> getValidNetworkInterfaces() throws SocketException {
        List<NetworkInterface> validNetworkInterfaces = new LinkedList();
        Enumeration interfaces = NetworkInterface.getNetworkInterfaces();

        while(interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = (NetworkInterface)interfaces.nextElement();
            if (!ignoreNetworkInterface(networkInterface)) {
                validNetworkInterfaces.add(networkInterface);
            }
        }

        return validNetworkInterfaces;
    }

    public static boolean isPreferredNetworkInterface(NetworkInterface networkInterface) {
        String preferredNetworkInterface = System.getProperty("dubbo.network.interface.preferred");
        return Objects.equals(networkInterface.getDisplayName(), preferredNetworkInterface);
    }
    public static String getIpByHost(String hostName) {
        try {
            return InetAddress.getByName(hostName).getHostAddress();
        } catch (UnknownHostException var2) {
            return hostName;
        }
    }

    public static String toAddressString(InetSocketAddress address) {
        return address.getAddress().getHostAddress() + ":" + address.getPort();
    }

    public static InetSocketAddress toAddress(String address) {
        int i = address.indexOf(58);
        String host;
        int port;
        if (i > -1) {
            host = address.substring(0, i);
            port = Integer.parseInt(address.substring(i + 1));
        } else {
            host = address;
            port = 0;
        }

        return new InetSocketAddress(host, port);
    }

    public static String toURL(String protocol, String host, int port, String path) {
        StringBuilder sb = new StringBuilder();
        sb.append(protocol).append("://");
        sb.append(host).append(':').append(port);
        if (path.charAt(0) != '/') {
            sb.append('/');
        }

        sb.append(path);
        return sb.toString();
    }

    public static void joinMulticastGroup(MulticastSocket multicastSocket, InetAddress multicastAddress) throws IOException {
        setInterface(multicastSocket, multicastAddress instanceof Inet6Address);
        multicastSocket.setLoopbackMode(false);
        multicastSocket.joinGroup(multicastAddress);
    }

    public static void setInterface(MulticastSocket multicastSocket, boolean preferIpv6) throws IOException {
        boolean interfaceSet = false;
        Enumeration interfaces = NetworkInterface.getNetworkInterfaces();

        while(interfaces.hasMoreElements()) {
            NetworkInterface i = (NetworkInterface)interfaces.nextElement();
            Enumeration addresses = i.getInetAddresses();

            while(addresses.hasMoreElements()) {
                InetAddress address = (InetAddress)addresses.nextElement();
                if (preferIpv6 && address instanceof Inet6Address) {
                    try {
                        if (address.isReachable(100)) {
                            multicastSocket.setInterface(address);
                            interfaceSet = true;
                            break;
                        }
                    } catch (IOException var8) {
                    }
                } else if (!preferIpv6 && address instanceof Inet4Address) {
                    try {
                        if (address.isReachable(100)) {
                            multicastSocket.setInterface(address);
                            interfaceSet = true;
                            break;
                        }
                    } catch (IOException var9) {
                    }
                }
            }

            if (interfaceSet) {
                break;
            }
        }

    }

    private static boolean ipPatternContainExpression(String pattern) {
        return pattern.contains("*") || pattern.contains("-");
    }

    private static void checkHostPattern(String pattern, String[] mask, boolean isIpv4) {
        if (!isIpv4) {
            if (mask.length != 8 && ipPatternContainExpression(pattern)) {
                throw new IllegalArgumentException("If you config ip expression that contains '*' or '-', please fill qualified ip pattern like 234e:0:4567:0:0:0:3d:*. ");
            }

            if (mask.length != 8 && !pattern.contains("::")) {
                throw new IllegalArgumentException("The host is ipv6, but the pattern is not ipv6 pattern : " + pattern);
            }
        } else if (mask.length != 4) {
            throw new IllegalArgumentException("The host is ipv4, but the pattern is not ipv4 pattern : " + pattern);
        }

    }

    private static String[] getPatternHostAndPort(String pattern, boolean isIpv4) {
        String[] result = new String[2];
        int end;
        if (pattern.startsWith("[") && pattern.contains("]:")) {
            end = pattern.indexOf("]:");
            result[0] = pattern.substring(1, end);
            result[1] = pattern.substring(end + 2);
            return result;
        } else if (pattern.startsWith("[") && pattern.endsWith("]")) {
            result[0] = pattern.substring(1, pattern.length() - 1);
            result[1] = null;
            return result;
        } else if (isIpv4 && pattern.contains(":")) {
            end = pattern.indexOf(":");
            result[0] = pattern.substring(0, end);
            result[1] = pattern.substring(end + 1);
            return result;
        } else {
            result[0] = pattern;
            return result;
        }
    }
}
