DROP DATABASE IF EXISTS simulation;
CREATE DATABASE simulation;
USE simulation;

-- Drop existing tables and recreate with clean schema
DROP TABLE IF EXISTS delivery_statistics;
DROP TABLE IF EXISTS counter_statistics;
DROP TABLE IF EXISTS kitchen_statistics;
DROP TABLE IF EXISTS reception_statistics;
DROP TABLE IF EXISTS overview_statistics;

-- Overview table with all SimulationStatistics fields
CREATE TABLE overview_statistics (
    id INT PRIMARY KEY AUTO_INCREMENT,
    
    -- SimulationStatistics fields
    simulation_time FLOAT,           -- Current simulation time
    total_simulation_time FLOAT,     -- Total simulation time
    call_in_mean_time FLOAT,         -- Mean time for call-in customers
    walk_in_mean_time FLOAT,         -- Mean time for walk-in customers
    total_arrived_customers INT,     -- Total customers who arrived
    total_serviced_customers INT,    -- Total customers serviced
    refused_delivery_customers INT,  -- Customers who refused delivery
    return_money_customers INT,      -- Customers who got money returned
    remake_orders_customers INT,     -- Customers whose orders were remade
    total_waiting_time FLOAT,        -- Sum of all waiting times
    system_throughput FLOAT,         -- System throughput
    average_response_time FLOAT,      -- Average response time
    busy_time FLOAT
);

-- Reception table with all ServicePointStatistics fields
CREATE TABLE reception_statistics (
    id INT PRIMARY KEY AUTO_INCREMENT,
    overview_id INT,
    
    -- ServicePointStatistics fields
    arrived_customers INT,           -- Number of customers who arrived at reception
    serviced_customers INT,          -- Number of customers serviced at reception
    service_busy_time FLOAT,         -- Time reception was busy serving customers
    service_utilization FLOAT,      -- Utilization rate of reception
    service_throughput FLOAT,        -- Throughput of reception
    average_service_time FLOAT,      -- Average time to serve a customer
    waiting_time FLOAT,              -- Total waiting time at reception
    average_queue_length FLOAT,      -- Average queue length at reception
    mean_value FLOAT,                -- Mean value for distribution
    variance_value FLOAT,            -- Variance value for distribution
    
    FOREIGN KEY (overview_id) REFERENCES overview_statistics(id)
);

-- Kitchen table with all ServicePointStatistics fields
CREATE TABLE kitchen_statistics (
    id INT PRIMARY KEY AUTO_INCREMENT,
    overview_id INT,
    
    -- ServicePointStatistics fields
    arrived_customers INT,           -- Number of orders that arrived at kitchen
    serviced_customers INT,          -- Number of orders completed by kitchen
    service_busy_time FLOAT,         -- Time kitchen was busy preparing orders
    service_utilization FLOAT,      -- Utilization rate of kitchen
    service_throughput FLOAT,        -- Throughput of kitchen
    average_service_time FLOAT,      -- Average time to prepare an order
    waiting_time FLOAT,              -- Total waiting time at kitchen
    average_queue_length FLOAT,      -- Average queue length at kitchen
    mean_value FLOAT,                -- Mean value for distribution
    variance_value FLOAT,            -- Variance value for distribution
    
    FOREIGN KEY (overview_id) REFERENCES overview_statistics(id)
);

-- Counter table with all ServicePointStatistics fields
CREATE TABLE counter_statistics (
    id INT PRIMARY KEY AUTO_INCREMENT,
    overview_id INT,
    
    -- ServicePointStatistics fields
    arrived_customers INT,           -- Number of customers who arrived at counter
    serviced_customers INT,          -- Number of customers served at counter
    service_busy_time FLOAT,         -- Time counter was busy serving customers
    service_utilization FLOAT,      -- Utilization rate of counter
    service_throughput FLOAT,        -- Throughput of counter
    average_service_time FLOAT,      -- Average time to serve a customer
    waiting_time FLOAT,              -- Total waiting time at counter
    average_queue_length FLOAT,      -- Average queue length at counter
    mean_value FLOAT,                -- Mean value for distribution
    variance_value FLOAT,            -- Variance value for distribution
    
    FOREIGN KEY (overview_id) REFERENCES overview_statistics(id)
);

-- Delivery table with all ServicePointStatistics fields
CREATE TABLE delivery_statistics (
    id INT PRIMARY KEY AUTO_INCREMENT,
    overview_id INT,
    
    -- ServicePointStatistics fields
    arrived_customers INT,           -- Number of delivery orders received
    serviced_customers INT,          -- Number of deliveries completed
    service_busy_time FLOAT,         -- Time delivery service was busy
    service_utilization FLOAT,      -- Utilization rate of delivery service
    service_throughput FLOAT,        -- Throughput of delivery service
    average_service_time FLOAT,      -- Average time to complete a delivery
    waiting_time FLOAT,              -- Total waiting time for delivery
    average_queue_length FLOAT,      -- Average queue length for delivery
    mean_value FLOAT,                -- Mean value for distribution
    variance_value FLOAT,            -- Variance value for distribution
    
    FOREIGN KEY (overview_id) REFERENCES overview_statistics(id)
);