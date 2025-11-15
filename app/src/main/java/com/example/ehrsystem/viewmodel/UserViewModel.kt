package com.example.ehrsystem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ehrsystem.database.EHRDatabase
import com.example.ehrsystem.database.EHRRepository
import com.example.ehrsystem.database.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EHRRepository
    val allUsers: LiveData<List<User>>

    init {
        val database = EHRDatabase.getDatabase(application)
        repository = EHRRepository(database)
        allUsers = repository.allUsers
    }

    fun insertUser(user: User, callback: (Boolean, String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.insertUser(user)
                if (result > 0) {
                    callback(true, "Registration successful!")
                } else {
                    callback(false, "Registration failed. Username may already exist.")
                }
            } catch (e: Exception) {
                callback(false, "Error: ${e.message}")
            }
        }
    }

    fun login(username: String, password: String, callback: (User?, String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = repository.login(username, password)
                if (user != null) {
                    callback(user, "Login successful!")
                } else {
                    callback(null, "Invalid username or password")
                }
            } catch (e: Exception) {
                callback(null, "Error: ${e.message}")
            }
        }
    }

    fun checkUsernameExists(username: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = repository.getUserByUsername(username)
                callback(user != null)
            } catch (e: Exception) {
                callback(false)
            }
        }
    }

    fun getUsersByRole(role: String): LiveData<List<User>> {
        return repository.getUsersByRole(role)
    }
}