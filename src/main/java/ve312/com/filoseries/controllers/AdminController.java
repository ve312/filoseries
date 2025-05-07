package ve312.com.filoseries.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ve312.com.filoseries.domain.Usuario;
import ve312.com.filoseries.service.UsuarioService;
import ve312.com.filoseries.util.MessageUtil;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {
    private final UsuarioService usuarioService;
    private final MessageUtil messageUtil;

    @Autowired
    public AdminController(UsuarioService usuarioService, MessageUtil messageUtil) {
        this.usuarioService = usuarioService;
        this.messageUtil = messageUtil;
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "admin/usuarios";
    }

    @PostMapping("/usuarios/{id}/cambiar-estado")
    public String cambiarEstadoUsuario(@PathVariable Long id,
                                       @RequestParam String estado,
                                       RedirectAttributes redirectAttributes) {

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEstado(estado);
            usuarioService.guardar(usuario);

            redirectAttributes.addFlashAttribute("mensaje",
                    messageUtil.getMessage("admin.usuario.estado.actualizado", usuario.getUsername(), estado));
        }

        return "redirect:/admin/usuarios";
    }

}
